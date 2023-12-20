package com.flynaut.healthtag.model.response

data class ShopListResponse(
    val Status: Int,
    val `data`: List<ShopProduct>,
    val message: String,
    val page: Int,
    val totalPages: Int
)

data class ShopProduct(
    val __v: Int,
    val _id: String,
    val category: String,
    val category_name: String,
    val coverImage: String,
    val deliveryRate: Int,
    val howToUse: String,
    val image: String,
    val isAddedCart: Boolean,
    val longDescription: String,
    val price: Int,
    val productName: String,
    val productType: String,
    var quantity: Int,
    val shortDescription: String,
    val status: String,
    val images: List<String>
)
