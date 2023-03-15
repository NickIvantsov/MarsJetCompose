package com.ivantsov.marsjetcompose.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.ivantsov.marsjetcompose.data.model.PhotoItem
import kotlinx.coroutines.launch

@Composable
fun MainHomeScreen(homeViewModel: HomeViewModel, modifier: Modifier = Modifier) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val photosLazyListStates = when (val state = uiState) {
        is HomeUiState.HasPhotos -> state.photoItemList
        is HomeUiState.NoPhotos -> {
            emptyList()
        }
    }
    val homeScreenType = getHomeScreenType(uiState)
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            LinearProgressIndicator()
        } else {
            when (homeScreenType) {
                HomeScreenType.START -> {
                    Button(onClick = {
                        homeViewModel.fetchPhotos()
                    }) {
                        Text(text = "Fetch Photos".uppercase())
                    }
                }
                HomeScreenType.PHOTOS -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(128.dp),

                        // content padding
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            top = 16.dp,
                            end = 12.dp,
                            bottom = 16.dp
                        ),
                        content = {
                            items(photosLazyListStates) { item ->
                                Card(
                                    backgroundColor = Color.LightGray,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth(),
                                    elevation = 8.dp,
                                ) {
                                    ImageCard(item)
                                }
                            }
                        }
                    )
                }
            }
        }

    }


}

@Composable
private fun ImageCard(item: PhotoItem) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader

    var isInProgress by remember { mutableStateOf(false) } //todo: add a support multiple stated of loading
    val coroutineScope = rememberCoroutineScope()
    val imageRequest = ImageRequest.Builder(context)
        .data(item.imgSrc)
        .listener(
            onStart = {
                isInProgress = true
            },
            onError = { img, error ->
                isInProgress = false
            },
            onCancel = {
                isInProgress = false
            },
            onSuccess = { request, metadata ->
                isInProgress = false
            }
        )
        .build()
    LaunchedEffect(key1 = item.id, block = {
        coroutineScope.launch {
            imageLoader.execute(imageRequest)
        }
    })


    val painter = rememberAsyncImagePainter(
        model = item.imgSrc,
        imageLoader = imageLoader
    )
    if (isInProgress) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator(
                color = Color.Gray,
                modifier = Modifier.width(75.dp)
            )
        }
    }
    Image(
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
    )


}