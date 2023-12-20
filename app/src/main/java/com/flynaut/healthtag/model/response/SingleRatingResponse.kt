package com.flynaut.healthtag.model.response

data class SingleRatingResponse(
    val Status: Int,
    val `data`: RatingData,
    val message: String
)
