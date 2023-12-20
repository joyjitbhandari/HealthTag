package com.flynaut.healthtag.model.request

data class AddFamilyMemberRequest(
    val name: String,
    val address: String,
    val addressLine2: String,
    val city: String,
    val state: String,
    val zipcode: String,
    val phoneNumber: String,
    val email: String,
    val relationship: String
)