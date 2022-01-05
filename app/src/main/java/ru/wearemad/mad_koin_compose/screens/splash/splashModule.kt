package ru.wearemad.mad_koin_compose.screens.splash

import org.koin.dsl.module
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val splashModule = module {

    scope<ScreenScope> {

        scoped {
            SplashVm(get(), get())
        }
    }
}