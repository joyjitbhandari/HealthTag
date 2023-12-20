package com.flynaut.healthtag.model.response

data class ProductDetailResponse(
    val Status: Int,
    val allRating: List<RatingData>,
    val averageRating: Float,
    val `data`: ProductDetail,
    val message: String
)
data class ProductDetail(
    val images: List<String>,
    val _id: String,
    val productName: String,
    val productType: String,
    val image: String,
    val coverImage: String,
    val price: Double,
    val deliveryRate: Int,
    val category: String,
    val category_name: String,
    val shortDescription: String,
    val longDescription: String,
    val howToUse: String,
    val quantity: Int,
    val status: String,
    val __v: Int,
)
data class RatingData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val productId: String,
    val rating: Int,
    val review: String,
    val user: String
)
