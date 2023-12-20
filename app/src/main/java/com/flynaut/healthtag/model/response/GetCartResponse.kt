package com.flynaut.healthtag.model.response

data class GetCartResponse(
    val `data`: List<ShopProduct>,
    val message: String,
    val status: Int,
    val total: String
)