package com.flynaut.healthtag.model.response

data class CartItemResponse(
    val `data`: List<CartListItem>,
    val message: String,
    val status: Int,
)
data class CartListItem(
    val __v: Int,
    val _id: String,
    val productId: ProductId,
    var quantity: Int,
    val userId: UserId
)
data class UserId(
    val __v: Int,
    val _id: String,
    val blocked: Boolean,
    val confirmPassword: String,
    val createdAt: String,
    val email: String,
    val isApproved: Boolean,
    val otp: String,
    val password: String,
    val refercode: String,
    val role: Int,
    val status: String,
    val updatedAt: String,
    val userId: Int
)

data class ProductId(
    val __v: Int,
    val _id: String,
    val category: String,
    val category_name: String,
    val coverImage: String,
    val deliveryRate: Int,
    val howToUse: String,
    val image: String,
    val longDescription: String,
    val price: Int,
    val productName: String,
    val productType: String,
    val quantity: Int,
    val shortDescription: String,
    val status: String
)