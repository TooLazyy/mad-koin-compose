package ru.wearemad.mad_koin_compose.utils

import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf

val LocalRootSaveableStateHolder = staticCompositionLocalOf<SaveableStateHolder?> {
    null
}