package ru.wearemad.mad_koin_compose.scopes

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getKoin
import org.koin.core.scope.Scope
import ru.wearemad.mad_compose_navigation.navigator.base.Navigator
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator
import ru.wearemad.mad_compose_navigation.router.provider.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.utils.createAppNestedNavigator
import ru.wearemad.mad_koin_compose.router.back_press.LocalRootBackPressedDispatcher
import ru.wearemad.mad_koin_compose.router.rememberNestedNavigator

@Composable
fun WithKoinScope(
    screenId: String,
    content: @Composable Scope.() -> Unit
) {
    val scope = getKoin().getOrCreateScope<Scope>(screenId)
    UpdateOpenedScopes(screenId)
    scope.content()
}

@Composable
fun WithKoinScopeFlow(
    rootNavigator: Navigator,
    screenId: String,
    content: @Composable Scope.(NestedNavigator) -> Unit,
    onBackPressedDispatcher: OnBackPressedDispatcher? = LocalRootBackPressedDispatcher.current,
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

    scope.content(nestedNavigator)
}

@Composable
private fun UpdateOpenedScopes(
    screenId: String,
) {
    LocalOpenedScopesHolder.current.addScreenScope(screenId)
}