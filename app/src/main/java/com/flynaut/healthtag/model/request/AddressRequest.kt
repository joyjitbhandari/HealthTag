package com.flynaut.healthtag.model.request

data class AddressRequest(
    val address: String,
    val addressType: String,
    val city: String,
    val phoneNumber: String,
    val state: String,
    val zipcode: String
)