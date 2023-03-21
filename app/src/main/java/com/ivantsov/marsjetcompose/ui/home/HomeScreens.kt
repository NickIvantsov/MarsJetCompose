package com.ivantsov.marsjetcompose.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivantsov.marsjetcompose.R
import com.ivantsov.marsjetcompose.util.net.NetworkObserver

@Composable
fun MainHomeScreen(
    homeViewModel: HomeViewModel,
    navigateToPhotos: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val networkState by homeViewModel.networkState.collectAsStateWithLifecycle()
    val networkErrorText = stringResource(R.string.error_network)
    var buttonState by remember { mutableStateOf(true) }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Button(enabled = buttonState,
                onClick = {
                    navigateToPhotos()
                }) {
                Text(text = stringResource(R.string.retch_photos).uppercase())
            }
        }

        LaunchedEffect("errorMessageText", "retryMessageText", networkState) {

            when (networkState) {
                is NetworkObserver.NetworkState.DISABLE -> {
                    buttonState = false
                    snackbarHostState.showSnackbar(message = networkErrorText)
                }
                is NetworkObserver.NetworkState.ENABLE -> {
                    buttonState = true
                }
            }
        }
    }

}