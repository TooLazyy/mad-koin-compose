package ru.wearemad.mad_koin_compose.screens.main

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_compose_navigation.impl.router.newRoot
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_koin_compose.screens.splash.SplashRoute
import ru.wearemad.mad_koin_compose.utils.ifWasNotCreatedBefore

class MainActivityVm(
    deps: VmDependencies,
    savedStateHandle: SavedStateHandle,
    private val router: Router
) : BaseVm<MainActivityState, VmEvent>(
    MainActivityState(),
    deps
) {

    init {
        savedStateHandle.ifWasNotCreatedBefore(
            actionIfNotSet = {
                launch(deps.dispatchers.main()) {
                    router.newRoot(SplashRoute())
                }
            }
        )
    }

    fun onCreated() {}
}