package com.ivantsov.marsjetcompose.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ivantsov.marsjetcompose.R
import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.data.reprository.PhotosRepository
import com.ivantsov.marsjetcompose.di.IoDispatcher
import com.ivantsov.marsjetcompose.util.ErrorMessage
import com.ivantsov.marsjetcompose.util.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val viewModelState = MutableStateFlow(PhotosViewModelState(isLoading = false))

    val uiState: StateFlow<PhotosUiState> = viewModelState
        .map(transform = PhotosViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    init {
        fetchPhotos()
    }

    fun fetchPhotos() {
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = withContext(ioDispatcher) {
                photosRepository.getPhotos()
            }

            viewModelState.update {

                when (result) {
                    is Outcome.Success -> {
                        it.copy(
                            isPhotosShowing = true,
                            photoItemList = result.value,
                            isLoading = false
                        )

                    }
                    is Outcome.Failure -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )

                        it.copy(
                            errorMessages = errorMessages,
                            isLoading = false,
                            isPhotosShowing = false
                        )
                    }
                }
            }
        }
    }

    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    // Define ViewModel factory in a companion object
    companion object {

        fun provideFactory(
            photosRepository: PhotosRepository,
            @IoDispatcher ioDispatcher: CoroutineDispatcher
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PhotosViewModel(photosRepository, ioDispatcher) as T
            }
        }
    }
}