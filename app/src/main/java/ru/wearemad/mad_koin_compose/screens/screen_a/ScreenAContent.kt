package ru.wearemad.mad_koin_compose.screens.screen_a

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_compose_navigation.impl.router.add
import ru.wearemad.mad_compose_navigation.impl.router.openDialog
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
        val data = remember {
            mutableStateOf("old value")
        }
        val data2 = rememberSaveable(
            id,
            stateSaver = object : Saver<String, Bundle> {

                override fun restore(value: Bundle): String? =
                    value.getString("data")

                override fun SaverScope.save(value: String): Bundle = Bundle().apply {
                    putString("data", value)
                }
            }
        ) {
            mutableStateOf("old value 2")
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
                .clickable {
                    vm.onClicked()
                }
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        data.value = "new value"
                        data2.value = "new value 2"
                    },
                text = buildAnnotatedString {
                    append("remember=")
                    append(data.value)
                    append(", saved=")
                    append(data2.value)
                },
                color = Color.Black
            )
        }
    }
}

@Stable
data class ScreenAState(
    override val loadingState: LoadingState = LoadingState.None
) : ViewState

class ScreenAVm(
    deps: VmDependencies,
    private val globalRouter: Router,
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
                                globalRouter.add(ScreenBRoute(), withAnimation = false)
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