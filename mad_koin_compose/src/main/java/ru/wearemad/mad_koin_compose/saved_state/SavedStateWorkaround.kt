package ru.wearemad.mad_koin_compose.saved_state

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import ru.wearemad.mad_koin_compose.utils.FlagWasCreatedBefore

/**
 * On android 13+, when pressing back button, onSaveInstanceState is called. Which triggers
 * saving state for navigator, scopes holders and so on.
 *
 * On pre-13, back press kills the activity and DOESNT trigger onSaveInstanceState.
 *
 * In both cases app instance remains alive.
 *
 * This library, is tied to system "save instance" calls.
 *
 * So I need this hack to keep screens and scopes alive when pressing back button on pre-13
 */
object SavedStateWorkaround {

    private var navigatorSavedState: Bundle? = null
    private var scopesSavedState: Bundle? = null
    private var mainVmSavedState: Bundle? = null

    fun provideMainVmState(): Bundle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Bundle()
    } else {
        mainVmSavedState ?: Bundle()
    }

    fun saveState(
        activity: ComponentActivity,
        navigatorSaver: () -> Bundle?,
        scopesSaver: () -> Bundle?,
    ) {
        if (activity.isFinishing.not() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return
        }
        mainVmSavedState = Bundle().apply {
            putBoolean(FlagWasCreatedBefore, true)
        }
        navigatorSavedState = navigatorSaver()
        scopesSavedState = scopesSaver()
    }

    fun restoreState(
        navigatorRestorer: (Bundle) -> Unit,
        scopesRestorer: (Bundle) -> Unit,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return
        }
        restoreInternal(
            navigatorSavedState,
            navigatorRestorer
        )
        restoreInternal(
            scopesSavedState,
            scopesRestorer
        )
    }

    private fun restoreInternal(
        state: Bundle?,
        restorer: (Bundle) -> Unit
    ) {
        state ?: return
        restorer(state)
    }
}