package ru.wearemad.mad_koin_compose.router

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import ru.wearemad.mad_compose_navigation.navigator.base.Navigator
import ru.wearemad.mad_compose_navigation.navigator.base.NavigatorState
import ru.wearemad.mad_compose_navigation.navigator.impl.RootNavigator
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator
import ru.wearemad.mad_compose_navigation.router.holder.RouterNavigatorHolder
import ru.wearemad.mad_compose_navigation.router.provider.RouterProvidersHolder
import ru.wearemad.mad_core_compose.utils.rememberLifecycleObserver
import ru.wearemad.mad_core_compose.vm.vm_store_holder.ComposeScreenViewModelStoreHolder
import ru.wearemad.mad_core_compose.vm.vm_store_holder.LocalComposeScreenViewModelStoreHolder
import ru.wearemad.mad_koin_compose.scopes.LocalOpenedScopesHolder
import ru.wearemad.mad_koin_compose.scopes.OpenedScopesHolder

@Composable
fun rememberNavigator(
    navigatorHolder: RouterNavigatorHolder,
    onBackPressedDispatcher: OnBackPressedDispatcher? = null,
    navigatorFactory: () -> RootNavigator,
    nestedNavigatorFactory: () -> NestedNavigator,
): Navigator {
    val factory = remember(onBackPressedDispatcher) {
        navigatorFactory
    }
    val navigator = rememberSaveable(
        onBackPressedDispatcher,
        saver = createNavigatorSaver(
            navigatorFactory = factory,
            nestedNavigatorFactory = nestedNavigatorFactory
        ),
        init = factory
    )
    val vmStoreHolder = LocalComposeScreenViewModelStoreHolder.current
    val routerProviderHolder = LocalRouterProvidersHolderProvider.current
    val openedScopesHolder = LocalOpenedScopesHolder.current
    val savedStateRegistryOwner = LocalContext.current as SavedStateRegistryOwner
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
            navigator
        )
    }
    return navigator
}

@Composable
fun Navigator.rememberNestedNavigator(
    navigatorHolder: RouterNavigatorHolder,
    key: String,
    onBackPressedDispatcher: OnBackPressedDispatcher? = null,
    factory: () -> NestedNavigator
): NestedNavigator {
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
    LaunchedEffect(key1 = onBackPressedDispatcher) {
        navigatorHolder.attachNavigator(navigator)
        navigator.registerOnBackPressedCallback(onBackPressedDispatcher)
        lifecycleOwner?.lifecycle?.addObserver(lifecycleObserver)
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
    navigatorFactory: () -> RootNavigator,
    nestedNavigatorFactory: () -> NestedNavigator,
): Saver<RootNavigator, Bundle> = Saver(
    save = {
        it.saveState()
    },
    restore = {
        navigatorFactory()
            .apply { restoreState(it, nestedNavigatorFactory) }
    }
)

private fun flattenNavigatorBackStack(rootState: NavigatorState): List<String> {
    val selfScreensIds = rootState
        .currentStack
        .map { it.screenKey }
    return selfScreensIds + rootState.nestedNavigatorsState
        .map { flattenNavigatorBackStack(it.state) }
        .flatten()
}

private fun CoroutineScope.subscribeToNavigatorAndCleanUnusedData(
    vmStoreHolder: ComposeScreenViewModelStoreHolder,
    openedScopesHolder: OpenedScopesHolder,
    routerProviderHolder: RouterProvidersHolder<*>,
    savedStateRegistry: SavedStateRegistry,
    navigator: Navigator
) {
    launch(Dispatchers.IO) {
        navigator
            .navigatorStateFlow
            .filter { it.stateChangedAtLeastOnce }
            .map(::flattenNavigatorBackStack)
            .map { screenIds ->
                val openedScopes = openedScopesHolder.openedScopes
                openedScopes
                    .filterNot { screenIds.contains(it) }
            }
            .collect { scopesToClose ->
                scopesToClose.forEach {
                    openedScopesHolder.removeScreenScope(it)
                    val scope = getKoin().getScope(it)
                    if (scope.isNotClosed()) {
                        scope.close()
                    }
                    routerProviderHolder.remove(it)
                    vmStoreHolder.clearScreenVmOwner(it)
                    savedStateRegistry.unregisterSavedStateProvider(it)
                }
            }
    }
}