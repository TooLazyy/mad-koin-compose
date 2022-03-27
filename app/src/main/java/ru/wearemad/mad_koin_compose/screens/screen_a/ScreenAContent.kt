package ru.wearemad.mad_koin_compose.screens.screen_a

import android.os.Bundle
import android.util.Log
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
import ru.wearemad.mad_compose_navigation.router.openDialog
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVm
import ru.wearemad.mad_koin_compose.screens.common_alert.CommonAlertResult
import ru.wearemad.mad_koin_compose.screens.common_alert.CommonAlertRoute
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

    companion object {

        private const val REQUEST_SCREEN_A_ALERT_1 = "request_screen_a_alert_1"
    }

    init {
        launch {
            resultsFlow
                .collect {
                    when (it.key.key) {
                        REQUEST_SCREEN_A_ALERT_1 -> {
                            val data = it.castData<CommonAlertResult>()
                            if (data.posivite) {
                                globalRouter.add(ScreenBRoute())
                            }
                            Log.d("MIINE", "result from alert $data")
                        }
                    }
                }
        }
        registerResultKeys(REQUEST_SCREEN_A_ALERT_1)
    }

    fun onClicked() {
        viewModelScope.launch {
            globalRouter.openDialog(CommonAlertRoute(REQUEST_SCREEN_A_ALERT_1))
        }
    }
}