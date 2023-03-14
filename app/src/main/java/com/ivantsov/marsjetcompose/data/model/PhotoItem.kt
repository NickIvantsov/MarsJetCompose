package com.ivantsov.marsjetcompose.data.model

import com.squareup.moshi.Json


/**
 * It's a data class with two properties, one of which is annotated with a custom name.
 * @property id - The photo's ID.
 * @property imgSrc - The URL of the image.
 */
data class PhotoItem(
    val id: String,
    @Json(name = "img_src")
    val imgSrc: String
)