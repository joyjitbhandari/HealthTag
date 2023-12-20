package com.flynaut.healthtag.model.request

data class AddToCartRequest(
    val userId: String,
    val productId: String,
    val quantity: Int
)