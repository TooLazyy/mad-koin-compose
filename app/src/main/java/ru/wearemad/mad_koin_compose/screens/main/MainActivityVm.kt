package ru.wearemad.mad_koin_compose.screens.main

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.router.Router
import ru.wearemad.mad_compose_navigation.router.newRoot
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_koin_compose.screens.splash.SplashRoute

class MainActivityVm(
    deps: VmDependencies,
    savedStateHandle: SavedStateHandle,
    private val router: Router
) : BaseVm<MainActivityState, VmEvent>(
    MainActivityState(),
    deps
) {

    init {
        val wasCreatedOnce = savedStateHandle.get<Boolean>("created") ?: false
        if (wasCreatedOnce.not()) {
            savedStateHandle["created"] = true
            launch {
                router.newRoot(SplashRoute())
            }
        }
    }

    fun onCreated() {}
}