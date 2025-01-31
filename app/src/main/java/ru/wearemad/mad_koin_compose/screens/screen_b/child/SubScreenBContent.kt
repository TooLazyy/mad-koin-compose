package ru.wearemad.mad_koin_compose.screens.screen_b.child

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import ru.wearemad.mad_compose_navigation.impl.router.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_compose_navigation.impl.router.add
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVm

@Stable
data class SubScreenBState(
    val counter: Int = 0,
    override val loadingState: LoadingState = LoadingState.None
) : ViewState

class SubScreenBVm(
    deps: VmDependencies,
    private val parentId: String,
    private val holder: DefaultRouterProvidersHolder,
    private val savedStateHandle: SavedStateHandle
) : BaseVm<SubScreenBState, VmEvent>(
    SubScreenBState(counter = savedStateHandle.get<Int>("counter") ?: 0),
    deps
) {

    private val parentRouter: Router?
        get() = holder.getRouterOrNull(parentId)

    fun onCounterClicked() {
        mutateStateAsync { state ->
            savedStateHandle.set("counter", state.counter + 1)
            state.copy(
                counter = state.counter + 1
            )
        }
    }

    fun onOpenChildClicked() {
        launch {
            parentRouter?.add(SubScreenBRoute(parentId, colorsList.random()), withAnimation = false)
        }
    }
}

@Composable
fun SubScreenBContent(id: String, args: Bundle?) {
    val parentId = args?.getString("data", "") ?: ""
    val textToShow = remember(id) {
        args?.getString("data2", "") ?: ""
    }
    WithKoinScopedVm(
        screenId = id,
        vmClass = SubScreenBVm::class,
        vmParameters = {
            parametersOf(parentId)
        }
    ) { vm ->
        val counter = vm.stateFlow.collectAsState().value.counter
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
                .clickable {
                    vm.onOpenChildClicked()
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                modifier = Modifier.clickable {
                    vm.onCounterClicked()
                },
                text = "$textToShow $counter"
            )
        }
    }
}