package com.ivantsov.marsjetcompose.util

import androidx.annotation.StringRes

inline fun <T> withTry(block: () -> T): Outcome<T> {
    return try {
        Outcome.Success(block())
    } catch (ex: Throwable) {
        Outcome.Failure(ex.message, ex)
    }
}


data class ErrorMessage(val id: Long, @StringRes val messageId: Int)