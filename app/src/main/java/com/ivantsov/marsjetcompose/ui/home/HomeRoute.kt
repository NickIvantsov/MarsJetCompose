package com.ivantsov.marsjetcompose.ui.home

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    navigateToPhotos: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    MainHomeScreen(
        homeViewModel = homeViewModel,
        navigateToPhotos = navigateToPhotos,
        modifier = modifier,
        snackbarHostState = snackbarHostState
    )
}
