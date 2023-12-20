package com.flynaut.healthtag.model.response


data class CategoryResponse(
    val `data`: List<Category>,
    val message: String,
    val status: Int
)
