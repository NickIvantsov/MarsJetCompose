package com.ivantsov.marsjetcompose.ui.photos

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


@Composable
fun PhotosRoute(
    viewModel: PhotosViewModel,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    PhotosScreen(viewModel = viewModel, modifier = modifier)
}