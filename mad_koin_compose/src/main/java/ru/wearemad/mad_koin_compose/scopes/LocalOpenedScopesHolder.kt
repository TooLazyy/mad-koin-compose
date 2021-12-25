package ru.wearemad.mad_koin_compose.scopes

import androidx.compose.runtime.staticCompositionLocalOf
import ru.wearemad.mad_core_compose.utils.noLocalProvidedFor

val LocalOpenedScopesHolder =
    staticCompositionLocalOf<OpenedScopesHolder> {
        noLocalProvidedFor("LocalOpenedScopesHolder")
    }