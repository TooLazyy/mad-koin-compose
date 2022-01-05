package ru.wearemad.mad_koin_compose.screens.screen_b.child

import org.koin.dsl.module
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val subScreenBModule = module {

    scope<ScreenScope> {

        scoped { (parentId: String) ->
            SubScreenBVm(
                get(), parentId,
                get(), get()
            )
        }
    }
}