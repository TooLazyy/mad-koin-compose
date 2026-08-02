package ru.wearemad.mad_koin_compose.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateHolder
import ru.wearemad.mad_compose_navigation.api.navigator.data.StackEntry

/**
 * Invoke Route's content on SaveableStateHolder which allows to save state using rememberSaveable.
 * State ownership is keyed by the stack entry instance, so a screen reopened under the same
 * screenKey never inherits scope or saved state from a previous instance.
 */
@Composable
fun SaveableStateHolder.RenderRouteWithSaveableStateHolder(
    entry: StackEntry
) {
    SaveableStateProvider(key = entry.instanceId) {
        entry.route.content(entry.instanceId, entry.route.args)
    }
}