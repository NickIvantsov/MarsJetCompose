package com.ivantsov.marsjetcompose.util

/**
 * The Outcome sealed class is a Kotlin class that provides a way to represent the outcome of
 * an operation, such as a network request or a database query. The Outcome class is generic,
 * allowing you to specify the type of value that is returned on success. The Outcome class is
 * designed to be used with Kotlin's type-safe enums, which allows you to handle the different
 * outcomes in a concise and safe manner.
 *
 * To use the Outcome sealed class, you can create an instance of either the Success or Failure subclass.
 * Here are some examples:
 * // Create a Success instance with an Int value of 42
 * val success: Outcome<Int> = Outcome.Success(42)
 *
 * // Create a Failure instance with an error message and a null throwable
 * val failure: Outcome<Nothing> = Outcome.Failure("An error occurred", null)
 *
 * To handle the different outcomes, you can use a when expression, like this:
 *  when (outcome) {
 *       is Outcome.Success -> {
 *             // Handle success
 *             val value = outcome.value
 *             // ...
 *         }
 *       is Outcome.Failure -> {
 *              // Handle failure
 *              val message = outcome.message
 *              val throwable = outcome.throwable
 *              // ...
 *         }
 *  }
 */
sealed class Outcome<out T> {
    /**
     * This subclass represents a successful outcome of an operation.
     * It contains a single property value which holds the value returned on success.
     */
    data class Success<out R>(val value: R) : Outcome<R>()

    /**
     * This subclass represents a failed outcome of an operation. It contains one property:
     * throwable. The throwable property is a nullable Throwable that provides additional information
     * about the failure.
     *
     * @property throwable - The exception that caused the failure.
     */
    data class Failure(
        val throwable: Throwable?
    ) : Outcome<Nothing>()
}