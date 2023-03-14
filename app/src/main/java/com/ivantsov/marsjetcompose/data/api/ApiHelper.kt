package com.ivantsov.marsjetcompose.data.api

import com.ivantsov.marsjetcompose.data.model.PhotoItem

interface ApiHelper {

    suspend fun getPhotos(): List<PhotoItem>

}