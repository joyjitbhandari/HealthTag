package com.flynaut.healthtag.model.response

data class AllRatingResponse(
    val Status: Int,
    val `data`: List<RatingData>,
    val message: String
)
