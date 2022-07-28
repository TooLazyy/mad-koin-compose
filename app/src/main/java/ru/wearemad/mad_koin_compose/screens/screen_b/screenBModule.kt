package ru.wearemad.mad_koin_compose.screens.screen_b

import org.koin.dsl.module
import ru.wearemad.mad_compose_navigation.utils.defaultNestedNavigatorFactory
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val screenBModule = module {

    scope<ScreenScope> {

        scoped { (id: String) ->
            ScreenBVm(
                get(), get(),
                id, get(),
                get()
            )
        }

        scoped {
            defaultNestedNavigatorFactory()
        }
    }
}