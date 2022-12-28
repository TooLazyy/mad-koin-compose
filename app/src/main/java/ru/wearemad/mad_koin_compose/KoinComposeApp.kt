package ru.wearemad.mad_koin_compose

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.wearemad.mad_koin_compose.di.messageControllerModule
import ru.wearemad.mad_koin_compose.di.navigationModule
import ru.wearemad.mad_koin_compose.di.vmModule
import ru.wearemad.mad_koin_compose.screens.common_alert.commonAlertModule
import ru.wearemad.mad_koin_compose.screens.main.mainModule
import ru.wearemad.mad_koin_compose.screens.screen_a.screenAModule
import ru.wearemad.mad_koin_compose.screens.screen_b.child.subScreenBModule
import ru.wearemad.mad_koin_compose.screens.screen_b.screenBModule
import ru.wearemad.mad_koin_compose.screens.splash.splashModule
import ru.wearemad.mad_koin_compose.screens.tabs_test.tab.tabsSubModule
import ru.wearemad.mad_koin_compose.screens.tabs_test.tabsMainModule

class KoinComposeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@KoinComposeApp)
            modules(
                listOf(
                    navigationModule,
                    messageControllerModule,
                    vmModule,
                    mainModule,
                    screenAModule,
                    splashModule,
                    screenBModule,
                    subScreenBModule,
                    commonAlertModule,
                    tabsMainModule,
                    tabsSubModule
                )
            )
        }
    }
}