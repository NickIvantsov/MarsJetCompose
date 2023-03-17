package com.ivantsov.marsjetcompose.util

import androidx.annotation.StringRes

inline fun <T> withTry(block: () -> T): Outcome<T> {
    return try {
        Outcome.Success(block())
    } catch (ex: Throwable) {
        Outcome.Failure(ex)
    }
}


data class ErrorMessage(val id: Long, @StringRes val messageId: Int)

/**
 * `ErrorInfo` is a data class that has two properties, `msg` and `error`, both of which are nullable.
 *
 * The `constructor` keyword is optional, but it's a good idea to include it. It makes it clear that
 * the following code is the constructor.
 *
 * @property msg - The error message.
 * @property error - The error type.
 */
data class ErrorInfo constructor(val msg: String? = null, val error: Throwable? = null)