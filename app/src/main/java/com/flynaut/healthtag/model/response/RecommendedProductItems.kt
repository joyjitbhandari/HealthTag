package com.flynaut.healthtag.model.response

data class RecommendedProductItems(
    val _id: String,
    val productName: String,
    val image: String,
    val price: Double,
    val category: String,
    val category_name: String,
    val quantity: Int,
    val __v: Int
)
