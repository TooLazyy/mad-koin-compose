package ru.wearemad.mad_koin_compose.screens.splash

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.router.Router
import ru.wearemad.mad_compose_navigation.router.add
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVm
import ru.wearemad.mad_koin_compose.screens.screen_a.ScreenARoute

@Composable
fun SplashContent(
    id: String,
    args: Bundle?
) {
    WithKoinScopedVm(
        screenId = id,
        vmClass = SplashVm::class,
    ) { vm ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .clickable {
                    vm.onClicked()
                }
        )
    }
}

@Stable
data class SplashState(
    override val loadingState: LoadingState = LoadingState.None
) : ViewState

class SplashVm(
    deps: VmDependencies,
    private val globalRouter: Router,
) : BaseVm<SplashState, VmEvent>(
    SplashState(),
    deps,
) {

    init {
        launch {
            resultsFlow
                .collect {
                    Log.d("MIINE", "Splash new result data: $it")
                }
        }
        registerResultKeys("key_1")
    }

    fun onClicked() {
        dependencies.messageController.showSnack(
            "some text",
            "go",
            SnackbarDuration.Indefinite
        ) {
            if (it == SnackbarResult.ActionPerformed) {
                launch {
                    globalRouter.add(ScreenARoute())
                }
            }
        }
    }
}