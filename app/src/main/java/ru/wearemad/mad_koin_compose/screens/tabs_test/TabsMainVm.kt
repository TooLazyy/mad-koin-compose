package ru.wearemad.mad_koin_compose.screens.tabs_test

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.impl.router.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ActivityLifecycleState
import ru.wearemad.mad_koin_compose.screens.tabs_test.navigation.replaceTab
import ru.wearemad.mad_koin_compose.screens.tabs_test.tab.TabsSubRoute

class TabsMainVm(
    deps: VmDependencies,
    private val savedStateHandle: SavedStateHandle,
    private val scopeId: String,
    private val globalRouter: Router,
    private val holder: DefaultRouterProvidersHolder,
) : BaseVm<TabsMainState, VmEvent>(
    TabsMainState(),
    deps,
) {

    private companion object {

        const val KEY_TAB = "key_tab"
    }

    private val flowRouter: Router
        get() = holder.getOrCreateRouter(scopeId)

    init {
        Log.d("MIINE", "TabsMainVm init")
        val savedTab = savedStateHandle.get<Int>(KEY_TAB) ?: 0
        openTab(savedTab)
    }

    fun onBottomItemClicked(tabId: Int) {
        mutateStateAsync { state ->
            state.copy(
                selectedTabId = tabId
            )
        }
        saveSelectedTab(tabId)
        openTab(tabId)
    }

    private fun saveSelectedTab(tabId: Int) {
        savedStateHandle[KEY_TAB] = tabId
    }

    private fun openTab(tabId: Int) {
        launch(dependencies.dispatchers.main()) {
            flowRouter.replaceTab(
                when (tabId) {
                    0 -> TabsSubRoute("first one")
                    1 -> TabsSubRoute("second one")
                    else -> throw RuntimeException("unknown id: $tabId")
                }
            )
        }
    }
}