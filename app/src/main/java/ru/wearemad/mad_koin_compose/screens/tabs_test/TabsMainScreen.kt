package ru.wearemad.mad_koin_compose.screens.tabs_test

import android.os.Bundle
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.core.parameter.parametersOf
import ru.wearemad.mad_koin_compose.content.RenderRouteWithSaveableStateHolder
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVmFlow
import ru.wearemad.mad_koin_compose.screens.main.LocalRootNavigator
import ru.wearemad.mad_koin_compose.utils.LocalRootSaveableStateHolder

@Composable
fun TabsMainScreen(
    id: String,
    args: Bundle?
) {
    val rootNavigator = LocalRootNavigator.current
    WithKoinScopedVmFlow(
        rootNavigator = rootNavigator,
        screenId = id,
        vmParameters = { parametersOf(id) },
        vmClass = TabsMainVm::class
    ) { nestedNavigator, vm ->
        val nestedState = nestedNavigator.stateFlow.collectAsState()
        val currentRoute = nestedState.value.currentRoute
        val state = vm.stateFlow.collectAsState()
        val saveableStateHolder = LocalRootSaveableStateHolder.current
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                if (currentRoute != null) {
                    Crossfade(
                        targetState = currentRoute,
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    ) {
                        saveableStateHolder?.RenderRouteWithSaveableStateHolder(it)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1F)
                        .background(
                            if (state.value.selectedTabId == 0) {
                                Color.Red
                            } else {
                                Color.Black
                            }
                        )
                        .clickable {
                            vm.onBottomItemClicked(0)
                        }
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1F)
                        .background(
                            if (state.value.selectedTabId == 1) {
                                Color.Red
                            } else {
                                Color.Black
                            }
                        )
                        .clickable {
                            vm.onBottomItemClicked(1)
                        }
                )
            }
        }
    }
}