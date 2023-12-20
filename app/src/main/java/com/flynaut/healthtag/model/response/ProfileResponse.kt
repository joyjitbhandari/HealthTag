package com.flynaut.healthtag.model.response


data class ProfileResponse(
    val Status: Int,
    val `data`: UserProfile,
    val message: String,
    val familyMemberCount: Int,
    val orderCount: Int,
    val addressCount: Int
)
data class UserProfile(
    val __v: Int,
    val _id: String,
    val blocked: Boolean,
    val confirmPassword: String,
    val createdAt: String,
    val email: String,
//    val familyMembers: List<FamilyMember>,
    val firstName: String,
    val gender: String,
    val height: String,
    val isApproved: Boolean,
    val languagePreference: String,
    val lastName: String,
    val otp: String,
    val password: String,
    val profilePicture: String,
    val refercode: String,
    val role: Int,
    val status: String,
    val updatedAt: String,
    val userId: Int,
    val weight: String,
    val phoneNumber: Long,
    val race: String,
    val dateOfBirth: String
)
