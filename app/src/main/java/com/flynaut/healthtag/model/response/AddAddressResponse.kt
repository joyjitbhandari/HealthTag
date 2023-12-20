package com.flynaut.healthtag.model.response

data class AddAddressResponse(
    val Status: Int,
    val `data`: Address,
    val message: String
)
