package ru.wearemad.mad_koin_compose.scopes

import android.os.Bundle

typealias ScreenScopes = MutableSet<String>

interface OpenedScopesHolder {

    val openedScopes: Set<String>

    fun removeScreenScope(screenId: String)

    fun addScreenScope(screenId: String)

    fun saveState(): Bundle

    fun restoreState(inState: Bundle)
}