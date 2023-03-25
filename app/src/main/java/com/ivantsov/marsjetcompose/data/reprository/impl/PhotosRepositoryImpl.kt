package com.ivantsov.marsjetcompose.data.reprository.impl

import com.ivantsov.marsjetcompose.data.api.ApiHelper
import com.ivantsov.marsjetcompose.data.reprository.PhotosRepository
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(private val apiHelper: ApiHelper) :
    PhotosRepository {
    override suspend fun getPhotos() = apiHelper.getPhotos()
}