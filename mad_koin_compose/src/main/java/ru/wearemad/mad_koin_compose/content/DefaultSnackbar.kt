package ru.wearemad.mad_koin_compose.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.wearemad.mad_core_compose.message.data.AppSnackState

@Composable
fun DefaultSnackbar(
    state: AppSnackState,
    modifier: Modifier = Modifier,
    snackContent: @Composable (payload: Any?) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = state,
        label = "DefaultSnackbar"
    ) { targetState ->
        when (targetState) {
            is AppSnackState.Hidden -> {}
            is AppSnackState.Visible -> {
                snackContent(targetState.payload)
            }
        }
    }
}