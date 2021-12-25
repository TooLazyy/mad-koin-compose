package ru.wearemad.mad_koin_compose.content

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.scope.Scope
import ru.wearemad.mad_compose_navigation.navigator.base.Navigator
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator
import ru.wearemad.mad_compose_navigation.router.provider.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.utils.createAppNestedNavigator
import ru.wearemad.mad_core_compose.message.rememberSnackbarHostState
import ru.wearemad.mad_core_compose.utils.SubscribeToLifecycle
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.router.back_press.LocalRootBackPressedDispatcher
import ru.wearemad.mad_koin_compose.router.rememberNestedNavigator
import ru.wearemad.mad_koin_compose.scopes.LocalOpenedScopesHolder
import ru.wearemad.mad_koin_compose.vm.getScopedViewModelByClass
import kotlin.reflect.KClass

/**
 * Creates Koin Scope for screen, remembers SnackbarHostState
 */
@Composable
fun WithKoinScope(
    screenId: String,
    modifier: Modifier = Modifier,
    snackContent: @Composable (SnackbarData) -> Unit = { Snackbar(it) },
    content: @Composable Scope.() -> Unit,
) {
    val scope = getKoin().getOrCreateScope<Scope>(screenId)
    UpdateOpenedScopes(screenId)

    val snackbarHostState = rememberSnackbarHostState(holder = get())

    Box(modifier = modifier) {
        scope.content()
        DefaultSnackbar(
            state = snackbarHostState,
            snackContent = snackContent
        )
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
    snackContent: @Composable (SnackbarData) -> Unit = { Snackbar(it) },
    content: @Composable Scope.(vm: Vm) -> Unit,
) {
    val scope = getKoin().getOrCreateScope<Scope>(screenId)
    UpdateOpenedScopes(screenId)

    val vm = scope.getScopedViewModelByClass(
        viewModelId = screenId,
        vmClass = vmClass,
        parameters = vmParameters
    )
    SubscribeToLifecycle(vm)

    val snackbarHostState = rememberSnackbarHostState(holder = get())

    Box(modifier = modifier) {
        scope.content(vm)
        DefaultSnackbar(
            state = snackbarHostState,
            snackContent = snackContent
        )
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
    snackContent: @Composable (SnackbarData) -> Unit = { Snackbar(it) },
    onBackPressedDispatcher: OnBackPressedDispatcher? = LocalRootBackPressedDispatcher.current,
    content: @Composable Scope.(NestedNavigator) -> Unit,
) {
    val scope = getKoin().getOrCreateScope<Scope>(screenId)
    UpdateOpenedScopes(screenId)

    val routerProviderHolder = get<DefaultRouterProvidersHolder>()
    val navigatorHolder = routerProviderHolder.getOrCreateHolder(screenId)
    val nestedNavigator = rootNavigator.rememberNestedNavigator(
        navigatorHolder = navigatorHolder,
        key = screenId,
        onBackPressedDispatcher = onBackPressedDispatcher,
        factory = { createAppNestedNavigator() }
    )

    val snackbarHostState = rememberSnackbarHostState(holder = get())

    Box(modifier = modifier) {
        scope.content(nestedNavigator)
        DefaultSnackbar(
            state = snackbarHostState,
            snackContent = snackContent
        )
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
    snackContent: @Composable (SnackbarData) -> Unit = { Snackbar(it) },
    onBackPressedDispatcher: OnBackPressedDispatcher? = LocalRootBackPressedDispatcher.current,
    content: @Composable Scope.(NestedNavigator, Vm) -> Unit,
) {
    val scope = getKoin().getOrCreateScope<Scope>(screenId)
    UpdateOpenedScopes(screenId)

    val routerProviderHolder = get<DefaultRouterProvidersHolder>()
    val navigatorHolder = routerProviderHolder.getOrCreateHolder(screenId)
    val nestedNavigator = rootNavigator.rememberNestedNavigator(
        navigatorHolder = navigatorHolder,
        key = screenId,
        onBackPressedDispatcher = onBackPressedDispatcher,
        factory = { createAppNestedNavigator() }
    )

    val vm = scope.getScopedViewModelByClass(
        viewModelId = screenId,
        vmClass = vmClass,
        parameters = vmParameters
    )
    SubscribeToLifecycle(vm)

    val snackbarHostState = rememberSnackbarHostState(holder = get())

    Box(modifier = modifier) {
        scope.content(nestedNavigator, vm)
        DefaultSnackbar(
            state = snackbarHostState,
            snackContent = snackContent
        )
    }
}

@Composable
private fun UpdateOpenedScopes(
    screenId: String,
) {
    LocalOpenedScopesHolder.current.addScreenScope(screenId)
}