package com.flynaut.healthtag.model.request

data class CreateChargeRequest(
    val email: String,
    val amount: String,
    val currency: String,
    val source: String,
    val description: String
)
//{
//    "email":"johsnnn@gmail.com",
//    "amount":10000,
//    "currency":"INR",
//    "source":"card_1NUvLCSHAab8BnVa55wnK62m",
//    "description":"demo payment"
//}