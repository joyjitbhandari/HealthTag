package com.flynaut.healthtag.model.response

data class MyOrderResponse(
    val `data`: List<MyOrder> ,
    val message: String,
    val status: Int
)
data class MyOrder(
    val __v: Int,
    val _id: String,
    val orderDate: String,
    val orderId: String,
    val paymentMethod: String,
    val products: List<ProductList>,
    val status: String,
    val totalAmount: Int,
    val userId: UserId
)

data class ProductList(
    val _id: String,
    val productId: ProductId,
    val quantity : Int
)

