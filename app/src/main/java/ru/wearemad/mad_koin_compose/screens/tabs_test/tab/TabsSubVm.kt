package ru.wearemad.mad_koin_compose.screens.tabs_test.tab

import android.util.Log
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent

class TabsSubVm(
    deps: VmDependencies,
    text: String,
) : BaseVm<TabsSubState, VmEvent>(
    TabsSubState(text = text),
    deps,
) {

    init {
        Log.d("MIINE", "init vm for: $text")
    }
}