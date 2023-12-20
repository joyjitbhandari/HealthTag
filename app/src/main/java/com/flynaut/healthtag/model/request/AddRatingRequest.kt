package com.flynaut.healthtag.model.request

data class AddRatingRequest(
    val rating: Int,
    val review: String
)