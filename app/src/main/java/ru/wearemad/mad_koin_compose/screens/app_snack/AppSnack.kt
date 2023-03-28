package ru.wearemad.mad_koin_compose.screens.app_snack

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.wearemad.mad_core_compose.message.data.AppSnackState
import ru.wearemad.mad_koin_compose.content.DefaultSnackbar

@Composable
fun BoxScope.AppSnack(
    state: AppSnackState,
    onClick: () -> Unit
) {
    DefaultSnackbar(
        modifier = Modifier.align(Alignment.BottomCenter),
        state = state,
        snackContent = { payload ->
            val snackData = payload as String
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        )
                    )
                    .background(Color.Cyan)
                    .clickable(onClick = onClick)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = snackData,
                )
            }
        }
    )
}