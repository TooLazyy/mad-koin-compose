package ru.wearemad.mad_koin_compose.screens.tabs_test.tab

import android.util.Log
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent

class TabsSubVm(
    deps: VmDependencies,
    private val text: String,
) : BaseVm<TabsSubState, VmEvent>(
    TabsSubState(text = text),
    deps,
) {

    init {
        Log.d("MIINE", "TabsSubVm init: $text")
    }

    override fun onCleared() {
        Log.d("MIINE", "TabsSubVm cleared: $text")
        super.onCleared()
    }
}