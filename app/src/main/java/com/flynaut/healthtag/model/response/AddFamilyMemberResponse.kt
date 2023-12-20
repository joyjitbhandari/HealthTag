package com.flynaut.healthtag.model.response

data class AddFamilyMemberResponse(
    val status: Int,
    val message: String,
    val `data`: FamilyMember
)
