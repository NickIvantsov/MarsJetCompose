package com.ivantsov.marsjetcompose.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainHomeScreen(homeViewModel: HomeViewModel, modifier: Modifier = Modifier) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val homeListLazyListState = rememberLazyListState()
    val photosLazyListStates = when (val state = uiState) {
        is HomeUiState.HasPhotos -> state.photoItemList
        is HomeUiState.NoPhotos -> emptyList()
    }
    val homeScreenType = getHomeScreenType(uiState)
    when (homeScreenType) {
        HomeScreenType.START -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    homeViewModel.fetchPhotos()
                }) {
                    Text(text = "Fetch Photos".uppercase())
                }

            }
        }
        HomeScreenType.PHOTOS -> {
            //todo
        }
    }

}