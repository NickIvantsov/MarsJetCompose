package com.ivantsov.marsjetcompose.ui.photos

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.ivantsov.marsjetcompose.R
import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.ui.home.ImageLoadingState
import com.ivantsov.marsjetcompose.util.ErrorInfo
import com.ivantsov.marsjetcompose.util.net.NetworkObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PhotosScreen(
    viewModel: PhotosViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val photoPager: LazyPagingItems<PhotoItem> = viewModel.photoPager.collectAsLazyPagingItems()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {

        when (photoPager.loadState.refresh) {
            LoadState.Loading -> {
                //show loading
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    LinearProgressIndicator()
                }
            }
            is LoadState.Error -> {
                //TODO implement error state
            }
            else -> {
                //show content
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it),
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
                            items(photoPager.itemCount) { item ->
                                Card(
                                    backgroundColor = Color.LightGray,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth(),
                                    elevation = 8.dp,
                                ) {
                                    photoPager[item]?.let { photo ->
                                        ImageCard(photo)
                                    }

                                }
                            }
                        }
                    )
                }
            }
        }
    }

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val networkErrorText = stringResource(R.string.error_network)
    LaunchedEffect("errorMessageText1", "retryMessageText1", networkState) {

        when (networkState) {
            is NetworkObserver.NetworkState.DISABLE -> {
                snackbarHostState.showSnackbar(
                    message = networkErrorText
                )
            }
            is NetworkObserver.NetworkState.ENABLE -> {

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