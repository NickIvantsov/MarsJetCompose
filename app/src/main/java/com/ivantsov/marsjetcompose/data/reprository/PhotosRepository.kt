package com.ivantsov.marsjetcompose.data.reprository

import com.ivantsov.marsjetcompose.data.api.ApiHelper
import javax.inject.Inject

class PhotosRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getPhotos() = apiHelper.getPhotos()
}