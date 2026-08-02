package ru.wearemad.mad_koin_compose.router

import android.os.Bundle
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
import ru.wearemad.mad_koin_compose.logger.MadKoinComposeLogger
import ru.wearemad.mad_koin_compose.scopes.LocalOpenedScopesHolder
import ru.wearemad.mad_koin_compose.scopes.OpenedScopesHolder
import ru.wearemad.mad_koin_compose.utils.LocalRootSaveableStateHolder

@Composable
fun rememberNavigator(
    navigatorHolder: RouterNavigatorHolder,
    onBackPressedDispatcher: OnBackPressedDispatcher? = null,
    navigatorFactory: NavigatorFactory,
    persistentScreenIds: () -> Set<String> = { emptySet() },
): Navigator {
    val rootFactory = remember(onBackPressedDispatcher) {
        {
            navigatorFactory.create(
                NavigatorFactoryParams.MainNavigatorParams("root")
            )
        }
    }
    val saver = remember(rootFactory, navigatorFactory) {
        createNavigatorSaver(
            rootNavigatorProvider = rootFactory,
            factory = navigatorFactory,
        )
    }
    val navigator = rememberSaveable(
        onBackPressedDispatcher,
        saver = saver,
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
            saveableStateHolder,
            persistentScreenIds,
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
    LaunchedEffect(onBackPressedDispatcher, navigator, navigatorHolder) {
        attachNavigator()
        launchEffectAction()
    }
    DisposableEffect(onBackPressedDispatcher, navigator, navigatorHolder) {
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

private fun NavigatorState.hasActiveAnimations(): Boolean =
    withAnimation || nestedNavigatorsState.any { it.hasActiveAnimations() }

private fun flattenNavigatorBackStack(rootState: NavigatorState): List<String> {
    val selfScreensIds = rootState
        .currentStack
        .map { it.instanceId } +
            rootState
                .currentDialogsStack
                .map { it.instanceId }
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
    saveableStateHolder: SaveableStateHolder?,
    persistentScreenIds: () -> Set<String>,
) {
    launch(Dispatchers.Main.immediate) {
        navigator
            .stateFlow
            .drop(1)
            .filterNot { it.hasActiveAnimations() }
            .map(::flattenNavigatorBackStack)
            .map { screenIds ->
                val persistentIds = persistentScreenIds()
                val openedScopes = openedScopesHolder.openedScopes
                MadKoinComposeLogger.d {
                    "STACK ids=${screenIds.joinToString()} | opened=${openedScopes.joinToString()}"
                }
                screenIds.toSet() to openedScopes
                    .filterNot { screenIds.contains(it) || persistentIds.contains(it) }
                    .toSet()
            }
            .collect { data ->
                val openedScreens = data.first + persistentScreenIds()
                val scopesToClose = data.second
                val closedVmScreens = vmStoreHolder.clearForUnusedScreens(openedScreens)
                val toClean = (closedVmScreens + scopesToClose).toSet()
                if (toClean.isNotEmpty()) {
                    MadKoinComposeLogger.d { "CLEANUP ${toClean.size}: ${toClean.joinToString()}" }
                }
                toClean.forEach {
                    openedScopesHolder.removeScreenScope(it)
                    val scope = getKoin().getScopeOrNull(it)
                    if (scope?.isNotClosed() == true) {
                        scope.close()
                        MadKoinComposeLogger.d { "CLOSE scope $it" }
                    } else {
                        MadKoinComposeLogger.d { "SKIP scope $it (missing or already closed)" }
                    }
                    routerProviderHolder.remove(it)
                    vmStoreHolder.clearScreenVmOwner(it)
                    savedStateRegistry.unregisterSavedStateProvider(it)
                    saveableStateHolder?.removeState(it)
                }
                if (toClean.isNotEmpty()) {
                    MadKoinComposeLogger.d {
                        "ALIVE ${openedScopesHolder.openedScopes.size}: ${openedScopesHolder.openedScopes.joinToString()}"
                    }
                }
            }
    }
}