package com.flynaut.healthtag.model.response

data class SignUpResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val token: String
)
data class Data(
    val __v: Int,
    val _id: String,
    val blocked: Boolean,
    val confirmPassword: String,
    val createdAt: String,
    val email: String,
    val isApproved: Boolean,
    val otp: String,
    val password: String,
    val refercode: String,
    val role: Int,
    val status: String,
    val updatedAt: String,
    val userId: Int,
    val familyMembers: List<FamilyMember>
    )
