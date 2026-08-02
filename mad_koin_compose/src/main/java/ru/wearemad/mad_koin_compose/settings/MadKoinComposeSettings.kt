package ru.wearemad.mad_koin_compose.settings

/**
 * Library-wide runtime settings. Set from the host app before navigation starts,
 * e.g. in Application.onCreate:
 *
 * MadKoinComposeSettings.logLevel = MadKoinComposeLogLevel.Enabled
 */
object MadKoinComposeSettings {

    @Volatile
    var logLevel: MadKoinComposeLogLevel = MadKoinComposeLogLevel.None
}