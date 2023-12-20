package com.flynaut.healthtag.model.response

data class AddCartResponse(
    val `data`: CartProduct,
    val message: String,
    val status: Int
)

data class CartProduct(
    val __v: Int,
    val _id: String,
    val image: String,
    val price: Double,
    val productName: String,
    val quantity: Int,
    val shop: String,
    val user: String
)