package com.ivantsov.marsjetcompose.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivantsov.marsjetcompose.R

@Composable
fun MainHomeScreen(
    homeViewModel: HomeViewModel,
    navigateToPhotos: () -> Unit,
    modifier: Modifier = Modifier
) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            navigateToPhotos()
        }) {
            Text(text = stringResource(R.string.retch_photos).uppercase())
        }
    }
}