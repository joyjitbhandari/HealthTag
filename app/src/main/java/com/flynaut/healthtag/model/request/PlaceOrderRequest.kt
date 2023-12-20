package com.flynaut.healthtag.model.request

data class PlaceOrderRequest(
    val addressId: String,
    val paymentMethod: String

)
///{
//    "addressId":"64b5159581d942e4dc53ed7b",
//    "paymentMethod":"online"
//}