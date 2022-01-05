package ru.wearemad.mad_koin_compose.screens.screen_a

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.router.Router
import ru.wearemad.mad_compose_navigation.router.add
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVm
import ru.wearemad.mad_koin_compose.screens.screen_b.ScreenBRoute

@Composable
fun ScreenAContent(id: String, args: Bundle?) {
    WithKoinScopedVm(
        screenId = id,
        vmClass = ScreenAVm::class
    ) { vm ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
                .clickable {
                    vm.onClicked()
                }
        )
    }
}

@Stable
data class ScreenAState(
    override val loadingState: LoadingState = LoadingState.None
) : ViewState

class ScreenAVm(
    deps: VmDependencies,
    private val globalRouter: Router
) : BaseVm<ScreenAState, VmEvent>(
    ScreenAState(),
    deps
) {

    fun onClicked() {
        viewModelScope.launch {
            globalRouter.add(ScreenBRoute())
        }
    }
}