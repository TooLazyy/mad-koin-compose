package ru.wearemad.mad_koin_compose.screens.tabs_test

import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState

data class TabsMainState(
    val selectedTabId: Int = 0,
    override val loadingState: LoadingState = LoadingState.None
) : ViewState