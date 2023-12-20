package com.flynaut.healthtag.model.response

data class ProviderResponse(
    val message: String,
    val `data`: List<UserProviders>,
    val providerDetails: List<ProviderDetails>
)

data class ProviderDetails(
    val _id: String,
    val image: String,
    val name: String,
    val specialty: String,
    val tenant: String,
    val __v: Int
)

data class UserProviders(
    val _id: String,
    val user:String,
    val providerId: String,
    val status: String
)

data class SingleProviderResponse(
    val `data`: ProviderDetails
)
