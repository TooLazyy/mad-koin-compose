package ru.wearemad.mad_koin_compose.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import ru.wearemad.mad_compose_navigation.route.Route

/**
 * Invoke Route's content on SaveableStateHolder which allows to save state using rememberSaveable
 */
@Composable
fun RenderRouteWithSaveableStateHolder(
    route: Route
) {
    val restorableStateHolder = rememberSaveableStateHolder()
    restorableStateHolder.SaveableStateProvider(key = route.screenKey) {
        route.content(route.screenKey, route.args)
    }
}