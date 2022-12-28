package ru.wearemad.mad_koin_compose.screens.tabs_test.tab

import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState

data class TabsSubState(
    val text: String,
    override val loadingState: LoadingState = LoadingState.None
) : ViewState