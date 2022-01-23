package ru.wearemad.mad_koin_compose.screens.common_alert

import org.koin.dsl.module
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val commonAlertModule = module {

    scope<ScreenScope> {

        scoped { (requestKey: String) ->
            CommonAlertVm(get(), requestKey, get())
        }
    }
}