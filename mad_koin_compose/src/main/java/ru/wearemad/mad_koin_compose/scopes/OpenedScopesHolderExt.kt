package ru.wearemad.mad_koin_compose.scopes

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberOpenedScopesHolder(
    factory: () -> OpenedScopesHolder = { DefaultOpenedScopesHolder() }
): OpenedScopesHolder = rememberSaveable(
    Unit,
    saver = createOpenedScopesHolderSaver(
        factory
    ),
    init = factory
)

private fun createOpenedScopesHolderSaver(
    factory: () -> OpenedScopesHolder
): Saver<OpenedScopesHolder, Bundle> = Saver(
    save = {
        it.saveState()
    },
    restore = {
        factory()
            .apply { restoreState(it) }
    }
)