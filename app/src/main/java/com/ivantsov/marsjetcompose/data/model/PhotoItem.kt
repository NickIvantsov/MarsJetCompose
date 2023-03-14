package com.ivantsov.marsjetcompose.data.model

import com.squareup.moshi.Json


data class PhotoItem(
    val id: String,
    @Json(name = "img_src")
    val imgSrc: String
)