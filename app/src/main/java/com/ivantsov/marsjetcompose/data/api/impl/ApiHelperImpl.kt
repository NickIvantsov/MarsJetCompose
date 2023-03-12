package com.ivantsov.marsjetcompose.data.api.impl

import com.ivantsov.marsjetcompose.data.api.ApiHelper
import com.ivantsov.marsjetcompose.data.api.ApiService
import com.ivantsov.marsphotos.data.model.PhotoItem

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPhotos(): List<PhotoItem> {
        return apiService.getPhotos()
    }
}