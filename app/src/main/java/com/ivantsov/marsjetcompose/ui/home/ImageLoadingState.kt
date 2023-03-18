package com.ivantsov.marsjetcompose.ui.home

import com.ivantsov.marsjetcompose.util.ErrorInfo

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