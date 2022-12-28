package ru.wearemad.mad_koin_compose.screens.tabs_test

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.impl.router.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_koin_compose.screens.tabs_test.navigation.replaceTab
import ru.wearemad.mad_koin_compose.screens.tabs_test.tab.TabsSubRoute
import ru.wearemad.mad_koin_compose.utils.ifFlagNotSetBefore

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

        const val KEY_CREATED = "key_created"
        const val KEY_TAB = "key_tab"
    }

    private val flowRouter: Router
        get() = holder.getOrCreateRouter(scopeId)

    init {
        flowRouter.hashCode()
        savedStateHandle.ifFlagNotSetBefore(
            KEY_CREATED,
            actionIfNotSet = {
                val currentOpenTab = 0
                openTab(currentOpenTab)
            }
        )
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