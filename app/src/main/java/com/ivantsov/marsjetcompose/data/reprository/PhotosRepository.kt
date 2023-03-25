package com.ivantsov.marsjetcompose.data.reprository

import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.util.Outcome

interface PhotosRepository {
    suspend fun getPhotos(): Outcome<List<PhotoItem>>
}