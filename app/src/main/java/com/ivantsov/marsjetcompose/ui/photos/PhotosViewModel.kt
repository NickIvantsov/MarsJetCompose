package com.ivantsov.marsjetcompose.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.data.paging.PhotosPagingSource
import com.ivantsov.marsjetcompose.data.reprository.PhotosRepository
import com.ivantsov.marsjetcompose.di.IoDispatcher
import com.ivantsov.marsjetcompose.util.ErrorMessage
import com.ivantsov.marsjetcompose.util.net.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed interface PhotosUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class NoPhotos(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : PhotosUiState

    data class HasPhotos(
        val isPhotosShowing: Boolean,
        val photoItemList: List<PhotoItem>,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : PhotosUiState
}

private data class PhotosViewModelState(
    val isPhotosShowing: Boolean = false,
    val photoItemList: List<PhotoItem>? = null,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) {

    /**
     * Converts this [HomeViewModelState] into a more strongly typed [HomeUiState] for driving
     * the ui.
     */
    fun toUiState(): PhotosUiState = if (photoItemList == null) {
        PhotosUiState.NoPhotos(
            isLoading = isLoading, errorMessages = errorMessages
        )
    } else {
        PhotosUiState.HasPhotos(
            photoItemList = photoItemList,
            isLoading = isLoading,
            errorMessages = errorMessages,
            isPhotosShowing = isPhotosShowing
        )
    }
}

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    networkObserver: NetworkObserver,
) : ViewModel() {
    private val viewModelState = MutableStateFlow(PhotosViewModelState(isLoading = false))

    val uiState: StateFlow<PhotosUiState> = viewModelState
        .map(transform = PhotosViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    val networkState = networkObserver.watchNetwork()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    private val pagingConfig = PagingConfig(
        pageSize = 10,
        enablePlaceholders = false,
        initialLoadSize = 20,
        prefetchDistance = 5
    )
    val photoPager = Pager(
        config = pagingConfig,
        pagingSourceFactory = { PhotosPagingSource(photosRepository) }
    ).flow.cachedIn(viewModelScope)

}