package com.flynaut.healthtag.model.request

data class AddProviderRequest(
    val image: String,
    val name: String,
    val specialty: String,
    val tenant: String
)