package com.flynaut.healthtag.model.response

data class FamilyResponse(
    val `data`: UserFamily,
    val status: Int
)

data class UserFamily(
    val __v: Int,
    val _id: String,
    val blocked: Boolean,
    val createdAt: String,
    val email: String,
    val familyMembers: List<FamilyMember>,
    val isApproved: Boolean,
    val role: Int,
    val status: String,
    val updatedAt: String,
    val userId: Int
)

data class FamilyMember(
    val _id: String,
    val userId: Int,
    val name: String,
    val phoneNo: Long,
    val address: String,
    val addressLine2: String,
    val city: String,
    val state: String,
    val zipcode: String,
    val email: String,
    val otp: String,
    val role: Int,
    val isApproved: Boolean,
    val status: String,
    val blocked: Boolean,
    val relation: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)