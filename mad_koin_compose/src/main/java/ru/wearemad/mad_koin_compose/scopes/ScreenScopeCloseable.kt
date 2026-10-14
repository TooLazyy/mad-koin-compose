package ru.wearemad.mad_koin_compose.scopes

import org.koin.core.Koin
import ru.wearemad.mad_koin_compose.logger.MadKoinComposeLogger

internal class ScreenScopeCloseable(
    private val koin: Koin,
    private val screenId: String,
) : AutoCloseable {

    companion object {

        const val KEY = "ru.wearemad.mad_koin_compose.scopes.ScreenScopeCloseable"
    }

    override fun close() {
        val scope = koin.getScopeOrNull(screenId)
        if (scope == null || scope.closed) {
            return
        }
        scope.close()
        MadKoinComposeLogger.d { "CLOSE scope $screenId (vm store cleared)" }
    }
}