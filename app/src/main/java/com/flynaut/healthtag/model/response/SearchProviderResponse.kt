package com.flynaut.healthtag.model.response

data class SearchProviderResponse(
    val `data`: List<Provider>,
    val message: String,
    val status: Int
)

data class Provider(
    val __v: Int,
    val _id: String,
    val image: String,
    val name: String,
    val specialty: String,
    val tenant: String
)