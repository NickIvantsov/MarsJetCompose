package com.ivantsov.marsjetcompose.data.api

import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.util.Outcome

/**
 * The ApiHelper interface is a Kotlin interface that defines a method for fetching photos from an API.
 * The method is designed to be used with coroutines, as it is a suspending function that returns
 * a list of PhotoItem objects.
 */
interface ApiHelper {

    /**
     * "This function returns a list of PhotoItem objects, and it can be called from a coroutine."
     *
     * The suspend keyword tells the compiler that this function can be called from a coroutine
     */
    suspend fun getPhotos(): Outcome<List<PhotoItem>>

}