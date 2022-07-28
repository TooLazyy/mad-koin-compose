package ru.wearemad.mad_koin_compose.screens.common_alert

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.koin.core.parameter.parametersOf
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_compose_navigation.impl.router.back
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVm

@Composable
fun CommonAlertContent(
    id: String,
    args: Bundle?
) {
    WithKoinScopedVm(
        screenId = id,
        vmClass = CommonAlertVm::class,
        vmParameters = {
            parametersOf(args?.getString("data", "") ?: "")
        }
    ) { vm ->
        AlertDialog(
            onDismissRequest = vm::onDismissClicked,
            title = { Text(text = "Title") },
            text = { Text("Open next screen?") },
            confirmButton = {
                Button(
                    onClick = vm::onPositiveButtonClicked
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = vm::onNegativeButtonClicked
                ) {
                    Text("No")
                }
            }
        )
    }
}

@Stable
data class CommonAlertState(
    override val loadingState: LoadingState = LoadingState.None
) : ViewState

class CommonAlertVm(
    deps: VmDependencies,
    private val requestKey: String,
    private val router: Router
) : BaseVm<CommonAlertState, VmEvent>(
    CommonAlertState(),
    deps
) {

    fun onDismissClicked() {
        launch {
            router.back()
        }
    }

    fun onNegativeButtonClicked() {
        setResult(requestKey, CommonAlertResult(posivite = false))
        launch {
            router.back()
        }
    }

    fun onPositiveButtonClicked() {
        setResult(requestKey, CommonAlertResult(posivite = true))
        launch {
            router.back()
        }
    }
}

@Parcelize
data class CommonAlertResult(
    val posivite: Boolean
) : Parcelable