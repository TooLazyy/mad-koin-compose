package ru.wearemad.mad_koin_compose.scopes

import android.os.Bundle
import android.util.Log

class DefaultOpenedScopesHolder : OpenedScopesHolder {

    private companion object {

        private const val KEY_OPENED_SCOPES = "ru.wearemad.mad_koin_compose.scopes.OpenedScopesHolder"
    }

    private val openedScopesSet: ScreenScopes = mutableSetOf()

    override val openedScopes: Set<String>
        get() = openedScopesSet.toSet()

    init {
        Log.d("MIINE", "DefaultOpenedScopesHolder init")
    }

    override fun removeScreenScope(
        screenId: String
    ) {
        openedScopesSet.remove(screenId)
    }

    override fun addScreenScope(screenId: String) {
        openedScopesSet.add(screenId)
    }

    override fun saveState(): Bundle = Bundle().apply {
        putStringArray(
            KEY_OPENED_SCOPES,
            openedScopesSet.toTypedArray()
        )
    }

    override fun restoreState(inState: Bundle) {
        val scopes = inState.getStringArray(KEY_OPENED_SCOPES) ?: return
        openedScopesSet.addAll(
            scopes
        )
    }

    override fun clearAll() {
        Log.d("MIINE", "DefaultOpenedScopesHolder clear")
        openedScopesSet.clear()
    }
}