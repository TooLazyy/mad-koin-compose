package ru.wearemad.mad_koin_compose.screens.screen_a

import org.koin.dsl.module
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val screenAModule = module {

    scope<ScreenScope> {

        scoped {
            ScreenAVm(get(), get())
        }
    }
}