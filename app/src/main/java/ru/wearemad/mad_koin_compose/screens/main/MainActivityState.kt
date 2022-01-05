package ru.wearemad.mad_koin_compose.screens.main

import androidx.compose.runtime.Stable
import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState

@Stable
data class MainActivityState(
    override val loadingState: LoadingState = LoadingState.None
) : ViewState