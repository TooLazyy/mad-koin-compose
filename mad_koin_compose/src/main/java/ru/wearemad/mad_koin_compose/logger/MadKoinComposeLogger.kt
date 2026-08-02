package ru.wearemad.mad_koin_compose.logger

import android.util.Log
import ru.wearemad.mad_koin_compose.settings.MadKoinComposeLogLevel
import ru.wearemad.mad_koin_compose.settings.MadKoinComposeSettings

private const val Tag = "MadKoinComposeLog"

/**
 * Library-wide logging. Messages are built lazily, so nothing is allocated
 * while MadKoinComposeSettings.logLevel is None.
 */
internal object MadKoinComposeLogger {

    fun d(message: () -> String) {
        if (isEnabled()) {
            Log.d(Tag, message())
        }
    }

    fun e(message: () -> String) {
        if (isEnabled()) {
            Log.e(Tag, message())
        }
    }

    private fun isEnabled(): Boolean =
        MadKoinComposeSettings.logLevel == MadKoinComposeLogLevel.Enabled
}