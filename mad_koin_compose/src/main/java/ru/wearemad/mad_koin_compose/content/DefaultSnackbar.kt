package ru.wearemad.mad_koin_compose.content

import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultSnackbar(
    state: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackContent: @Composable (SnackbarData) -> Unit = { Snackbar(it) }
) {
    SnackbarHost(
        hostState = state,
        modifier = modifier,
        snackbar = snackContent
    )
}