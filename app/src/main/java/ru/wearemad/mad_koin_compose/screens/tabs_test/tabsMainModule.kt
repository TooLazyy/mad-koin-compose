package ru.wearemad.mad_koin_compose.screens.tabs_test

import org.koin.dsl.module
import ru.wearemad.mad_koin_compose.scopes.ScreenScope
import ru.wearemad.mad_koin_compose.screens.tabs_test.navigation.tabsFlowNavigatorFactory

val tabsMainModule = module {

    scope<ScreenScope> {

        scoped { (scopeId: String) ->
            TabsMainVm(
                get(), get(),
                scopeId, get(),
                get(),
            )
        }

        scoped {
            tabsFlowNavigatorFactory()
        }
    }
}