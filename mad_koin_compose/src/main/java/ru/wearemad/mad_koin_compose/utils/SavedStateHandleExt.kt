package ru.wearemad.mad_koin_compose.utils

import androidx.lifecycle.SavedStateHandle

const val FlagWasCreatedBefore = "FlagWasCreatedBefore"

fun SavedStateHandle.ifFlagNotSetBefore(
    key: String,
    actionIfNotSet: () -> Unit
) {
    val flag = get(key) ?: false
    if (flag) {
        return
    }
    set(key, true)
    actionIfNotSet()
}

fun SavedStateHandle.ifWasNotCreatedBefore(
    actionIfNotSet: () -> Unit
) {
    ifFlagNotSetBefore(
        FlagWasCreatedBefore,
        actionIfNotSet
    )
}