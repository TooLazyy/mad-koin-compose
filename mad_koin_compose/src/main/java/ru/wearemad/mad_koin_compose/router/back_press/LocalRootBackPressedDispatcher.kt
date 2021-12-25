package ru.wearemad.mad_koin_compose.router.back_press

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.staticCompositionLocalOf
import ru.wearemad.mad_core_compose.utils.noLocalProvidedFor

val LocalRootBackPressedDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> {
    noLocalProvidedFor("AmbientBackPressedDispatcher")
}