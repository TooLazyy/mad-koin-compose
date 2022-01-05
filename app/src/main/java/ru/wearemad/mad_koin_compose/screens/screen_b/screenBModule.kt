package ru.wearemad.mad_koin_compose.screens.screen_b

import org.koin.dsl.module
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val screenBModule = module {

    scope<ScreenScope> {

        scoped { (id: String) ->
            ScreenBVm(
                get(), id,
                get(), get()
            )
        }
    }
}