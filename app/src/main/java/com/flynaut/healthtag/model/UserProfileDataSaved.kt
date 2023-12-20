package com.flynaut.healthtag.model

data class UserProfileDataSaved(
    val _id : String,
    val firstName : String,
    val lastName : String,
    val email : String,
    val profilePicture: String,
    var _paymentId: String
)
