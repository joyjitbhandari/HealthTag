package com.flynaut.healthtag.model.response

data class FeedbackResponse(
    val status: Int,
    val message: String,
    val data: FeedbackData
)

data class FeedbackData(
    val user: String,
    val feedback: String,
    val emoji: Int,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)