package com.flynaut.healthtag.model.response

data class AddressResponse(
    val status: Int,
    val `data`: List<Address>,
    val message: String
)

data class Address(
    val __v: Int,
    val _id: String,
    val address: String,
    val addressLane2: String,
    val addressType: String,
    val phoneNumber: String,
    val city: String,
    val default: Boolean,
    val state: String,
    val user: String,
    val zipcode: String
)