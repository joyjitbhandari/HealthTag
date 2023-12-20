package com.flynaut.healthtag.model.response

data class AllSubscriptionDetailsResponse(
    val `data`: List<SubscriptionDetails>,
    val status: Int
)

data class SubscriptionDetails(
    val _id: String,
    val user: String,
    val plan: String,
    val startDate: String,
    val endDate: String,
    val price: Int,
    val status: String,
    val __v: Int
)