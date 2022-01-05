package ru.wearemad.mad_koin_compose.di

import org.koin.dsl.module
import ru.wearemad.mad_base.coroutines.DefaultDispatchersProvider
import ru.wearemad.mad_core_compose.message.ComposeMessageControllerProvider
import ru.wearemad.mad_core_compose.message.wrapper.DefaultComposeMessageControllerWrapper
import ru.wearemad.mad_core_compose.vm.dependencies.DefaultVmDependencies
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.lifecycle.DefaultScreenLifecycleObserver
import ru.wearemad.mad_koin_compose.scopes.ScreenScope

val vmModule = module {

    scope<ScreenScope> {

        scoped<VmDependencies> {
            val provider = get<ComposeMessageControllerProvider<DefaultComposeMessageControllerWrapper>>()
            DefaultVmDependencies(
                DefaultDispatchersProvider(),
                provider.messageController,
                DefaultScreenLifecycleObserver()
            )
        }
    }

    factory<VmDependencies> {
        val provider = get<ComposeMessageControllerProvider<DefaultComposeMessageControllerWrapper>>()
        DefaultVmDependencies(
            DefaultDispatchersProvider(),
            provider.messageController,
            DefaultScreenLifecycleObserver()
        )
    }
}