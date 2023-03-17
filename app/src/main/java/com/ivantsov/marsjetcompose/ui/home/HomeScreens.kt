package com.ivantsov.marsjetcompose.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.ivantsov.marsjetcompose.R
import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.util.ErrorInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainHomeScreen(
    homeViewModel: HomeViewModel, modifier: Modifier = Modifier
) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val photosLazyListStates = when (val state = uiState) {
        is HomeUiState.HasPhotos -> state.photoItemList
        is HomeUiState.NoPhotos -> {
            emptyList()
        }
    }
    val homeScreenType = getHomeScreenType(uiState)

    when (homeScreenType) {
        HomeScreenType.START -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    LinearProgressIndicator()
                } else {
                    Button(onClick = {
                        homeViewModel.fetchPhotos()
                    }) {
                        Text(text = stringResource(R.string.retch_photos).uppercase())
                    }
                }
            }
            printDebugLog("uiState.errorMessages: ${uiState.errorMessages.joinToString()}")
        }
        HomeScreenType.PHOTOS -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
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

@Composable
private fun ImageCard(
    item: PhotoItem,
    context: Context = LocalContext.current,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
    workerCoroutineScope: CoroutineScope = rememberCoroutineScope()
) {

    var imgLoadingState: ImageLoadingState by remember { mutableStateOf(ImageLoadingState.Ideal) }

    val imageRequest = ImageRequest.Builder(context)
        .data(item.imgSrc)
        .listener(
            onStart = {
                imgLoadingState = ImageLoadingState.StartRequest
            },
            onError = { img, error ->
                imgLoadingState = ImageLoadingState.RequestError(
                    errorInfo = ErrorInfo(
                        error.throwable.message,
                        error.throwable
                    )
                )
            },
            onCancel = {
                imgLoadingState = ImageLoadingState.RequestCanceled
            },
            onSuccess = { request, metadata ->
                imgLoadingState = ImageLoadingState.RequestSuccess
            }
        )
        .build()
    LaunchedEffect(key1 = item.id, block = {
        workerCoroutineScope.launch { imageLoader.execute(imageRequest) }
    })


    val painter = rememberAsyncImagePainter(
        model = item.imgSrc,
        imageLoader = imageLoader,
        fallback = null, //todo: need to process a fallback case
        error = rememberVectorPainter(Icons.Default.Warning),
        onLoading = {
            imgLoadingState = ImageLoadingState.PainterLoading
        },
        onSuccess = {
            imgLoadingState = ImageLoadingState.PainterSuccess
        },
        onError = {
            imgLoadingState = ImageLoadingState.PainterError(
                errorInfo = ErrorInfo(
                    msg = it.result.throwable.message,
                    error = it.result.throwable
                )
            )
        }
    )
    when (imgLoadingState) {

        ImageLoadingState.Ideal -> {
            //do nothing
        }
        is ImageLoadingState.PainterError -> {
            //todo handle the painter error
        }
        ImageLoadingState.RequestCanceled -> {
            //todo think about this scenario
        }
        is ImageLoadingState.RequestError -> {
            //todo handle the request error
        }

        ImageLoadingState.RequestSuccess, ImageLoadingState.PainterSuccess -> {
            ImageCardContent(painter)
        }

        ImageLoadingState.StartRequest, ImageLoadingState.PainterLoading -> {
            ImageCardBackgroundBox()
        }
    }
}

@Composable
private fun ImageCardBackgroundBox() {
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

@Composable
private fun ImageCardContent(painter: Painter) {
    Image(
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
    )
}

@Preview
@Composable
fun ImageCardBackgroundBoxPreview() {
    ImageCardBackgroundBox()
}

@Preview
@Composable
fun ImageCardContentPreview() {
    ImageCardContent(ColorPainter(color = Color.Red))
}


private fun printDebugLog(msg: String) {
    Log.d("TAG_1", msg)
}

sealed class ImageLoadingState {
    object Ideal : ImageLoadingState()
    object StartRequest : ImageLoadingState()
    data class RequestError(val errorInfo: ErrorInfo? = null) : ImageLoadingState()
    object RequestCanceled : ImageLoadingState()
    object RequestSuccess : ImageLoadingState()
    object PainterSuccess : ImageLoadingState()
    object PainterLoading : ImageLoadingState()

    /**
     * `PainterError` is a data class that has one property, `errorInfo`, which is of type `ErrorInfo?`
     * and has a default value of `null`.
     * @property errorInfo - This is an object that contains information about the error.
     */
    data class PainterError(val errorInfo: ErrorInfo? = null) : ImageLoadingState()
}