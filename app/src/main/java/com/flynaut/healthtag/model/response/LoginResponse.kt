package com.flynaut.healthtag.model.response
data class LoginResponse(
    val `data`: UserData,
    val message: String,
    val status: Int,
    val token: String
)

data class UserData(
    val _id : String,
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val userProfile: UserProfile,
    val role: Int,
    val familyMembers: List<FamilyMember>,
)