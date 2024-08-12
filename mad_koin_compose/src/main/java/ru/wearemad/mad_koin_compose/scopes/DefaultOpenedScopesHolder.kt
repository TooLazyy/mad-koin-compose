package ru.wearemad.mad_koin_compose.scopes

import android.os.Bundle

class DefaultOpenedScopesHolder : OpenedScopesHolder {

    private companion object {

        private const val KEY_OPENED_SCOPES = "ru.wearemad.mad_koin_compose.scopes.OpenedScopesHolder"
    }

    @get:Synchronized
    private val openedScopesSet: ScreenScopes = mutableSetOf()

    @get:Synchronized
    override val openedScopes: Set<String>
        get() = openedScopesSet.toSet()

    @Synchronized
    override fun removeScreenScope(
        screenId: String
    ) {
        openedScopesSet.remove(screenId)
    }

    @Synchronized
    override fun addScreenScope(screenId: String) {
        openedScopesSet.add(screenId)
    }

    @Synchronized
    override fun saveState(): Bundle = Bundle().apply {
        putStringArray(
            KEY_OPENED_SCOPES,
            openedScopesSet.toTypedArray()
        )
    }

    @Synchronized
    override fun restoreState(inState: Bundle) {
        val scopes = inState.getStringArray(KEY_OPENED_SCOPES) ?: return
        openedScopesSet.addAll(
            scopes
        )
    }

    @Synchronized
    override fun clearAll() {
        openedScopesSet.clear()
    }
}