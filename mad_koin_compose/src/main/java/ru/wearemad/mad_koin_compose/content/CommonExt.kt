package ru.wearemad.mad_koin_compose.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateHolder
import ru.wearemad.mad_compose_navigation.api.route.Route

/**
 * Invoke Route's content on SaveableStateHolder which allows to save state using rememberSaveable
 */
@Composable
fun SaveableStateHolder.RenderRouteWithSaveableStateHolder(
    route: Route
) {
    SaveableStateProvider(key = route.screenKey) {
        route.content(route.screenKey, route.args)
    }
}