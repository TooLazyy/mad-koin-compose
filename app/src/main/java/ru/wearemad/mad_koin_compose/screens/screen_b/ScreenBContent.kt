package ru.wearemad.mad_koin_compose.screens.screen_b

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.koin.core.parameter.parametersOf
import ru.wearemad.mad_compose_navigation.impl.router.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_compose_navigation.impl.router.add
import ru.wearemad.mad_compose_navigation.impl.router.newRoot
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.content.RenderRouteWithSaveableStateHolder
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVmFlow
import ru.wearemad.mad_koin_compose.screens.main.LocalRootNavigator
import ru.wearemad.mad_koin_compose.screens.screen_b.child.SubScreenBRoute
import ru.wearemad.mad_koin_compose.utils.ifWasNotCreatedBefore

@Composable
fun ScreenBContent(id: String, args: Bundle?) {
    val rootNavigator = LocalRootNavigator.current
    WithKoinScopedVmFlow(
        rootNavigator = rootNavigator,
        screenId = id,
        vmParameters = {
            parametersOf(id)
        },
        vmClass = ScreenBVm::class,
        content = { nestedNavigator, vm ->
            Log.d("MIINE", "ScreenBContent: $id")
            val nestedState = nestedNavigator.stateFlow.collectAsState()
            val currentRoute = nestedState.value.currentRoute
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable {
                        vm.onRootClicked()
                    }
                    .padding(top = 190.dp)
            ) {
                if (currentRoute != null) {
                    RenderRouteWithSaveableStateHolder(currentRoute)
                }
            }
        }
    )
}

@Stable
data class ScreenBState(
    override val loadingState: LoadingState = LoadingState.None
) : ViewState

class ScreenBVm(
    deps: VmDependencies,
    savedStateHandle: SavedStateHandle,
    private val scopeId: String,
    private val holder: DefaultRouterProvidersHolder,
    private val globalRouter: Router
) : BaseVm<ScreenBState, VmEvent>(
    ScreenBState(),
    deps
) {

    private val flowRouter: Router
        get() = holder.getOrCreateRouter(scopeId)

    init {
        Log.d("MIINE", "ScreenBVm")
        savedStateHandle.ifWasNotCreatedBefore(
            actionIfNotSet = {
                launch {
                    flowRouter.newRoot(SubScreenBRoute(scopeId))
                }
            }
        )
    }

    fun onRootClicked() {
        setResult("key_1", ScreenBResult("from screen a"))
        viewModelScope.launch {
            globalRouter.add(ScreenBRoute())
        }
    }
}

@Parcelize
class ScreenBResult(
    val someValue: String
) : Parcelable