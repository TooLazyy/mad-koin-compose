package ru.wearemad.mad_koin_compose.router

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorState
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactoryParams
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.router.RouterNavigatorHolder
import ru.wearemad.mad_compose_navigation.api.router.RouterProvidersHolder
import ru.wearemad.mad_core_compose.utils.rememberLifecycleObserver
import ru.wearemad.mad_core_compose.vm.vm_store_holder.ComposeScreenViewModelStoreHolder
import ru.wearemad.mad_core_compose.vm.vm_store_holder.LocalComposeScreenViewModelStoreHolder
import ru.wearemad.mad_koin_compose.scopes.LocalOpenedScopesHolder
import ru.wearemad.mad_koin_compose.scopes.OpenedScopesHolder
import ru.wearemad.mad_koin_compose.utils.LocalRootSaveableStateHolder

@Composable
fun rememberNavigator(
    navigatorHolder: RouterNavigatorHolder,
    onBackPressedDispatcher: OnBackPressedDispatcher? = null,
    navigatorFactory: NavigatorFactory,
): Navigator {
    val rootFactory = remember(onBackPressedDispatcher) {
        {
            navigatorFactory.create(
                NavigatorFactoryParams.MainNavigatorParams("root")
            )
        }
    }
    val navigator = rememberSaveable(
        onBackPressedDispatcher,
        saver = createNavigatorSaver(
            rootNavigatorProvider = rootFactory,
            factory = navigatorFactory,
        ),
        init = rootFactory
    )
    val vmStoreHolder = LocalComposeScreenViewModelStoreHolder.current
    val routerProviderHolder = LocalRouterProvidersHolderProvider.current
    val openedScopesHolder = LocalOpenedScopesHolder.current
    val savedStateRegistryOwner = LocalContext.current as SavedStateRegistryOwner
    val saveableStateHolder = LocalRootSaveableStateHolder.current
    AttachNavigatorToLifecycle(
        navigatorHolder,
        navigator,
        onBackPressedDispatcher
    ) {
        subscribeToNavigatorAndCleanUnusedData(
            vmStoreHolder,
            openedScopesHolder,
            routerProviderHolder,
            savedStateRegistryOwner.savedStateRegistry,
            navigator,
            saveableStateHolder
        )
    }
    return navigator
}

@Composable
fun Navigator.rememberNestedNavigator(
    navigatorHolder: RouterNavigatorHolder,
    key: String,
    onBackPressedDispatcher: OnBackPressedDispatcher? = null,
    factory: NestedNavigatorFactory
): Navigator {
    val navigator = remember(this, key) {
        getOrCreateNestedNavigator(key, factory)
    }
    AttachNavigatorToLifecycle(
        navigatorHolder,
        navigator,
        onBackPressedDispatcher,
    ) {}
    return navigator
}

@Composable
private fun AttachNavigatorToLifecycle(
    navigatorHolder: RouterNavigatorHolder,
    navigator: Navigator,
    onBackPressedDispatcher: OnBackPressedDispatcher? = null,
    launchEffectAction: CoroutineScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalContext.current as? LifecycleOwner
    val lifecycleObserver = rememberLifecycleObserver(
        key = onBackPressedDispatcher,
        onPause = {
            navigatorHolder.detachNavigator()
        },
        onResume = {
            coroutineScope.launch {
                navigatorHolder.attachNavigator(navigator)
            }
        }
    )

    suspend fun attachNavigator() {
        navigatorHolder.attachNavigator(navigator)
        navigator.registerOnBackPressedCallback(onBackPressedDispatcher)
        lifecycleOwner?.lifecycle?.addObserver(lifecycleObserver)
    }
    LaunchedEffect(key1 = onBackPressedDispatcher) {
        attachNavigator()
        launchEffectAction()
    }
    DisposableEffect(onBackPressedDispatcher) {
        onDispose {
            navigatorHolder.detachNavigator()
            navigator.unregisterOnBackPressedCallback()
            lifecycleOwner?.lifecycle?.removeObserver(lifecycleObserver)
        }
    }
}

private fun createNavigatorSaver(
    rootNavigatorProvider: () -> Navigator,
    factory: NavigatorFactory,
): Saver<Navigator, Bundle> = Saver(
    save = {
        it.saveState()
    },
    restore = {
        rootNavigatorProvider()
            .apply { restoreState(it, factory) }
    }
)

private fun flattenNavigatorBackStack(rootState: NavigatorState): List<String> {
    val selfScreensIds = rootState
        .currentStack
        .map { it.screenKey } +
            rootState
                .currentDialogsStack
                .map { it.screenKey }
    return selfScreensIds + rootState.nestedNavigatorsState
        .map { flattenNavigatorBackStack(it) }
        .flatten()
}

private fun CoroutineScope.subscribeToNavigatorAndCleanUnusedData(
    vmStoreHolder: ComposeScreenViewModelStoreHolder,
    openedScopesHolder: OpenedScopesHolder,
    routerProviderHolder: RouterProvidersHolder<*>,
    savedStateRegistry: SavedStateRegistry,
    navigator: Navigator,
    saveableStateHolder: SaveableStateHolder?
) {
    launch(Dispatchers.Main.immediate) {
        navigator
            .stateFlow
            .drop(1)
            .filterNot { it.withAnimation }
            .map(::flattenNavigatorBackStack)
            .map { screenIds ->
                val openedScopes = openedScopesHolder.openedScopes
                Log.d(
                    "NavigatorExt",
                    "subscribeToNavigatorAndCleanUnusedData. map ids=${screenIds.joinToString()}, openedScopes=${openedScopes.joinToString()}"
                )
                screenIds.toSet() to openedScopes
                    .filterNot { screenIds.contains(it) }
            }
            .collect { data ->
                val scopesToClose = data.second
                val openedScreens = data.first
                scopesToClose.forEach {
                    openedScopesHolder.removeScreenScope(it)
                    val scope = getKoin().getScopeOrNull(it)
                    if (scope?.isNotClosed() == true) {
                        scope.close()
                    }
                    routerProviderHolder.remove(it)
                    vmStoreHolder.clearScreenVmOwner(it)
                    savedStateRegistry.unregisterSavedStateProvider(it)
                    saveableStateHolder?.removeState(it)
                }
                vmStoreHolder.clearForUnusedScreens(openedScreens)
            }
    }
}