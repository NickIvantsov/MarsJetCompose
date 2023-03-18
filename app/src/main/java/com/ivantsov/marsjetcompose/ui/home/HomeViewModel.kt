package com.ivantsov.marsjetcompose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ivantsov.marsjetcompose.util.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

sealed interface HomeUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class Default(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : HomeUiState
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) {
    fun toUiState(): HomeUiState = HomeUiState.Default(
        isLoading = isLoading, errorMessages = errorMessages
    )
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = false))

    val uiState: StateFlow<HomeUiState> = viewModelState
        .map(transform = HomeViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    // Define ViewModel factory in a companion object
    companion object {

        fun provideFactory(
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel() as T
            }
        }
    }
}