package com.flynaut.healthtag.model.response

data class FamilyMemberResponse(
    val status: Int,
    val `data`: UserFamilyData
)

data class UserFamilyData(
    val familyMembers: List<FamilyMembers>
)

data class FamilyMembers(
    val _id: Int,
    val userId: Int,
    val name: String,
    val address: String,
    val email: String,
    val role: Int,
    val relation: String
)