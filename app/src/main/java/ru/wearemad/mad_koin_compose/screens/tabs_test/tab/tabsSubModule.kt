package ru.wearemad.mad_koin_compose.screens.tabs_test.tab

import org.koin.dsl.module
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val tabsSubModule = module {

    scope<ScreenScope> {

        scoped { (text: String) ->
            TabsSubVm(
                get(), text
            )
        }
    }
}