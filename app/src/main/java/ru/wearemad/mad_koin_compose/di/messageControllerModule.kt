package ru.wearemad.mad_koin_compose.di

import org.koin.dsl.module
import ru.wearemad.mad_core_compose.message.ComposeMessageControllerProvider
import ru.wearemad.mad_core_compose.message.wrapper.DefaultComposeMessageControllerWrapper
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val messageControllerModule = module {

    /**
     * for main activity
     */
    factory {
        ComposeMessageControllerProvider.create()
    }

    factory {
        val provider =
            get<ComposeMessageControllerProvider<DefaultComposeMessageControllerWrapper>>()
        provider.messageController.holder
    }

    /**
     * for scoped screens
     */
    scope<ScreenScope> {

        scoped {
            ComposeMessageControllerProvider.create()
        }

        scoped {
            val provider =
                get<ComposeMessageControllerProvider<DefaultComposeMessageControllerWrapper>>()
            provider.messageController.holder
        }
    }
}