package ru.wearemad.mad_koin_compose.screens.tabs_test.tab

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.core.parameter.parametersOf
import ru.wearemad.mad_koin_compose.content.WithKoinScopedVm
import ru.wearemad.mad_koin_compose.screens.main.LocalRootNavigator

@Composable
fun TabsSubScreen(
    id: String,
    args: Bundle?
) {
    WithKoinScopedVm(
        screenId = id,
        vmParameters = {
            parametersOf(
                args?.getString("data", "") ?: "none"
            )
        },
        vmClass = TabsSubVm::class
    ) { vm ->
        val state = vm.stateFlow.collectAsState()
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = state.value.text
            )
        }
    }
}