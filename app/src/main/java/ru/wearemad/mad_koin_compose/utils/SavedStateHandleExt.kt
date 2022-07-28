package ru.wearemad.mad_koin_compose.utils

import androidx.lifecycle.SavedStateHandle

fun SavedStateHandle.ifFlagNotSetBefore(
    key: String = "wasCreated",
    actionIfNotSet: () -> Unit
) {
    val flag = get(key) ?: false
    if (flag) {
        return
    }
    set(key, true)
    actionIfNotSet()
}