package com.flynaut.healthtag.model.response
data class CancelOrderResponse(
    val `data`: CancelData,
    val message: String,
    val status: Int
)
data class CancelData(
    val __v: Int,
    val _id: String,
    val orderDate: String,
    val orderId: String,
    val paymentMethod: String,
    val products: List<CancelProduct>,
    val status: String,
    val totalAmount: Int,
    val userId: String
)
data class CancelProduct(
    val _id: String,
    val productId: String,
    val quantity: Int
)
