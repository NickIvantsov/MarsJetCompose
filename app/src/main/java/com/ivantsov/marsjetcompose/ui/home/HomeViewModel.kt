package com.ivantsov.marsjetcompose.ui.home

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ivantsov.marsjetcompose.R
import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.data.reprository.PhotosRepository
import com.ivantsov.marsjetcompose.util.ErrorMessage
import com.ivantsov.marsjetcompose.util.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed interface HomeUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class NoPhotos(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : HomeUiState

    data class HasPhotos(
        val isPhotosShowing: Boolean,
        val photoItemList: List<PhotoItem>,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : HomeUiState
}

private data class HomeViewModelState(
    val isPhotosShowing: Boolean = false,
    val photoItemList: List<PhotoItem>? = null,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) {

    /**
     * Converts this [HomeViewModelState] into a more strongly typed [HomeUiState] for driving
     * the ui.
     */
    fun toUiState(): HomeUiState = if (photoItemList == null) {
        HomeUiState.NoPhotos(
            isLoading = isLoading, errorMessages = errorMessages
        )
    } else {
        HomeUiState.HasPhotos(
            photoItemList = photoItemList,
            isLoading = isLoading,
            errorMessages = errorMessages,
            isPhotosShowing = isPhotosShowing
        )
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photosRepository: PhotosRepository
) : ViewModel() {
    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = false))

    val uiState: StateFlow<HomeUiState> = viewModelState
        .map(transform = HomeViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    fun fetchPhotos() {
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = photosRepository.getPhotos()
            viewModelState.update {

                when (result) {
                    is Outcome.Success -> {
                        Log.d("TAG_1", "it.photoItemList size: ${it.photoItemList?.size}")
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
                        it.copy(errorMessages = errorMessages, isLoading = false, isPhotosShowing = false)
                    }

                }
            }
        }
    }

    // Define ViewModel factory in a companion object
    companion object {

        fun provideFactory(
            photosRepository: PhotosRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(photosRepository) as T
            }
        }
    }
}

enum class HomeScreenType {
    START,
    PHOTOS
}

@Composable
fun getHomeScreenType(
    uiState: HomeUiState
): HomeScreenType = when (uiState) {
    is HomeUiState.HasPhotos -> {
        if (uiState.isPhotosShowing) {
            HomeScreenType.PHOTOS
        } else {
            HomeScreenType.START
        }
    }
    is HomeUiState.NoPhotos -> HomeScreenType.START
}