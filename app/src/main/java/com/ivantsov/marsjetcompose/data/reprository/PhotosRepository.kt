package com.ivantsov.marsjetcompose.data.reprository

import com.ivantsov.marsjetcompose.data.api.ApiHelper

class PhotosRepository(private val apiHelper: ApiHelper) {
    suspend fun getPhotos() = apiHelper.getPhotos()
}