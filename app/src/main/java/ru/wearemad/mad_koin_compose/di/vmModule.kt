package ru.wearemad.mad_koin_compose.di

import org.koin.dsl.module
import ru.wearemad.mad_core_compose.message.ComposeMessageControllerProvider
import ru.wearemad.mad_core_compose.message.wrapper.DefaultComposeMessageControllerWrapper
import ru.wearemad.mad_core_compose.result_handler.DefaultRequestResultStore
import ru.wearemad.mad_core_compose.result_handler.RequestResultStore
import ru.wearemad.mad_core_compose.vm.dependencies.DefaultVmDependencies
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.lifecycle.DefaultScreenLifecycleObserver
import ru.wearemad.mad_core_compose.vm.result_listener.DefaultVmRequestResultHandler
import ru.wearemad.mad_koin_compose.scopes.ScreenScope
import ru.wearemad.mad_utils.dispatcher.DefaultDispatchersProvider

val vmModule = module {

    single<RequestResultStore> { DefaultRequestResultStore() }

    scope<ScreenScope> {

        scoped<VmDependencies> {
            val provider = get<ComposeMessageControllerProvider<DefaultComposeMessageControllerWrapper>>()
            val dispatchers = DefaultDispatchersProvider()
            val lifecycleObserver = DefaultScreenLifecycleObserver()
            DefaultVmDependencies(
                dispatchers,
                provider.messageController,
                lifecycleObserver,
                DefaultVmRequestResultHandler(
                    dispatchers,
                    get(),
                    lifecycleObserver
                )
            )
        }
    }

    factory<VmDependencies> {
        val provider = get<ComposeMessageControllerProvider<DefaultComposeMessageControllerWrapper>>()
        val dispatchers = DefaultDispatchersProvider()
        val lifecycleObserver = DefaultScreenLifecycleObserver()
        DefaultVmDependencies(
            dispatchers,
            provider.messageController,
            lifecycleObserver,
            DefaultVmRequestResultHandler(
                dispatchers,
                get(),
                lifecycleObserver
            )
        )
    }
}