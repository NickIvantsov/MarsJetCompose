package com.ivantsov.marsjetcompose.data.api

import com.ivantsov.marsjetcompose.data.model.PhotoItem
import retrofit2.http.GET

interface ApiService {

    /**
     * It's a suspend function that returns a list of PhotoItem objects
     */
    @GET("photos")
    suspend fun getPhotos(): List<PhotoItem>
}