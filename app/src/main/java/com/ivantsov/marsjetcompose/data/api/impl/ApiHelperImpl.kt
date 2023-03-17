package com.ivantsov.marsjetcompose.data.api.impl

import com.ivantsov.marsjetcompose.data.api.ApiHelper
import com.ivantsov.marsjetcompose.data.api.ApiService
import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.util.Outcome
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPhotos(): Outcome<List<PhotoItem>> {
        return try {
            Outcome.Success(apiService.getPhotos())
        } catch (ex: Throwable) {
            Outcome.Failure(ex)
        }
    }
}