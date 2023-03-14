package com.ivantsov.marsjetcompose.data.api

import com.ivantsov.marsjetcompose.data.model.PhotoItem
import retrofit2.http.GET

interface ApiService {

    @GET("photos")
    suspend fun getPhotos(): List<PhotoItem>
}