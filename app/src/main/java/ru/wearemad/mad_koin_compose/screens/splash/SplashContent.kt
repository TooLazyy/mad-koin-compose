package ru.wearemad.mad_koin_compose.screens.splash

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_compose_navigation.impl.router.add
import ru.wearemad.mad_core_compose.message.data.AppShackData
import ru.wearemad.mad_core_compose.message.data.AppSnackDuration
import ru.wearemad.mad_core_compose.vm.core.BaseVm
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.state.LoadingState
import ru.wearemad.mad_core_compose.vm.state.ViewState
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVm
import ru.wearemad.mad_koin_compose.screens.app_snack.AppSnack
import ru.wearemad.mad_koin_compose.screens.screen_a.ScreenARoute
import ru.wearemad.mad_koin_compose.utils.LocalRootSaveableStateHolder

@Composable
fun SplashContent(
    id: String,
    args: Bundle?
) {
    WithKoinScopedVm(
        screenId = id,
        vmClass = SplashVm::class,
        snackContent = { state, vm ->
            AppSnack(
                state = state,
                onClick = vm::onSnackClicked
            )
        }
    ) { vm ->
        val items = remember {
            List(50) {
                "Item id=${it + 1}"
            }
        }
        val listState = rememberLazyListState()
        val holder = LocalRootSaveableStateHolder.current
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.CenterStart)
                            .clickable {
                                var a = holder.hashCode() +1
                                a += 1
                                vm.onSnackClicked()
                            },
                        text = item
                    )
                }
            }
        }
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
            AppShackData(
                "some text",
                AppSnackDuration.WithTime(1_500L)
            )
        )
    }

    fun onSnackClicked() {
        launch(dependencies.dispatchers.main()) {
            globalRouter.add(ScreenARoute(), withAnimation = false)
        }
    }
}