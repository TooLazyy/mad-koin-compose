package ru.wearemad.mad_koin_compose.content

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.scope.Scope
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.impl.router.DefaultRouterProvidersHolder
import ru.wearemad.mad_core_compose.message.data.AppSnackState
import ru.wearemad.mad_core_compose.message.rememberAppSnackState
import ru.wearemad.mad_core_compose.utils.SubscribeToLifecycle
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.router.back_press.LocalRootBackPressedDispatcher
import ru.wearemad.mad_koin_compose.router.rememberNestedNavigator
import ru.wearemad.mad_koin_compose.scopes.LocalOpenedScopesHolder
import ru.wearemad.mad_koin_compose.scopes.ScreenScope
import ru.wearemad.mad_koin_compose.vm.getScopedViewModelByClass
import kotlin.reflect.KClass

/**
 * Creates Koin Scope for screen, remembers SnackbarHostState
 */
@Composable
fun WithKoinScope(
    screenId: String,
    modifier: Modifier = Modifier,
    snackContent: @Composable BoxScope.(AppSnackState) -> Unit = { snackBarState ->
        DefaultSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = snackBarState,
            snackContent = {}
        )
    },
    content: @Composable Scope.() -> Unit,
) {
    val scope = getKoin().getOrCreateScope<ScreenScope>(screenId)
    UpdateOpenedScopes(screenId)

    val snackbarHostState = rememberAppSnackState(holder = scope.get())

    Box(modifier = modifier) {
        scope.content()
        snackContent(snackbarHostState)
    }
}

/**
 * Creates Koin Scope for screen, SnackbarHostState and Vm instance.
 * Subscribes vm instance to lifecycle
 */
@Composable
fun <State : ViewState, Event : VmEvent, Vm : BaseVm<State, Event>> WithKoinScopedVm(
    screenId: String,
    vmClass: KClass<Vm>,
    modifier: Modifier = Modifier,
    vmParameters: ParametersDefinition? = null,
    snackContent: @Composable BoxScope.(AppSnackState, Vm) -> Unit = { snackBarState, _ ->
        DefaultSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = snackBarState,
            snackContent = {}
        )
    },
    content: @Composable Scope.(vm: Vm) -> Unit,
) {
    val scope = getKoin().getOrCreateScope<ScreenScope>(screenId)
    UpdateOpenedScopes(screenId)

    val vm = scope.getScopedViewModelByClass(
        viewModelId = screenId,
        vmClass = vmClass,
        parameters = vmParameters
    )
    SubscribeToLifecycle(vm)

    val snackbarHostState = rememberAppSnackState(holder = scope.get())

    Box(modifier = modifier) {
        scope.content(vm)
        snackContent(snackbarHostState, vm)
    }
}

/**
 * Creates Koin Scope for flow screen, SnackbarHostState and flow navigator for nested navigation
 * Subscribes vm instance to lifecycle
 */
@Composable
fun WithKoinScopeFlow(
    rootNavigator: Navigator,
    screenId: String,
    modifier: Modifier = Modifier,
    onBackPressedDispatcher: OnBackPressedDispatcher? = LocalRootBackPressedDispatcher.current,
    snackContent: @Composable BoxScope.(AppSnackState) -> Unit = { snackBarState ->
        DefaultSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = snackBarState,
            snackContent = {}
        )
    },
    content: @Composable Scope.(Navigator) -> Unit,
) {
    val scope = getKoin().getOrCreateScope<ScreenScope>(screenId)
    UpdateOpenedScopes(screenId)

    val nestedNavigatorFactory = scope.get<NestedNavigatorFactory>()
    val routerProviderHolder = get<DefaultRouterProvidersHolder>()
    val navigatorHolder = routerProviderHolder.getOrCreateHolder(screenId)
    val nestedNavigator = rootNavigator.rememberNestedNavigator(
        navigatorHolder = navigatorHolder,
        key = screenId,
        onBackPressedDispatcher = onBackPressedDispatcher,
        factory = nestedNavigatorFactory
    )

    val snackbarHostState = rememberAppSnackState(holder = scope.get())

    Box(modifier = modifier) {
        scope.content(nestedNavigator)
        snackContent(snackbarHostState)
    }
}

/**
 * Creates Koin Scope for flow screen, SnackbarHostState, flow navigator for nested navigation and Vm instance
 * Subscribes vm instance to lifecycle
 */
@Composable
fun <State : ViewState, Event : VmEvent, Vm : BaseVm<State, Event>> WithKoinScopedVmFlow(
    rootNavigator: Navigator,
    screenId: String,
    vmClass: KClass<Vm>,
    modifier: Modifier = Modifier,
    vmParameters: ParametersDefinition? = null,
    onBackPressedDispatcher: OnBackPressedDispatcher? = LocalRootBackPressedDispatcher.current,
    snackContent: @Composable BoxScope.(AppSnackState, Vm) -> Unit = { snackBarState, _ ->
        DefaultSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = snackBarState,
            snackContent = {}
        )
    },
    content: @Composable Scope.(Navigator, Vm) -> Unit,
) {
    val scope = getKoin().getOrCreateScope<ScreenScope>(screenId)
    UpdateOpenedScopes(screenId)

    val nestedNavigatorFactory = scope.get<NestedNavigatorFactory>()
    val routerProviderHolder = get<DefaultRouterProvidersHolder>()
    val navigatorHolder = routerProviderHolder.getOrCreateHolder(screenId)
    val nestedNavigator = rootNavigator.rememberNestedNavigator(
        navigatorHolder = navigatorHolder,
        key = screenId,
        onBackPressedDispatcher = onBackPressedDispatcher,
        factory = nestedNavigatorFactory,
    )

    val vm = scope.getScopedViewModelByClass(
        viewModelId = screenId,
        vmClass = vmClass,
        parameters = vmParameters
    )
    SubscribeToLifecycle(vm)

    val snackbarHostState = rememberAppSnackState(holder = scope.get())

    Box(modifier = modifier) {
        scope.content(nestedNavigator, vm)
        snackContent(snackbarHostState, vm)
    }
}

@Composable
private fun UpdateOpenedScopes(
    screenId: String,
) {
    LocalOpenedScopesHolder.current.addScreenScope(screenId)
}