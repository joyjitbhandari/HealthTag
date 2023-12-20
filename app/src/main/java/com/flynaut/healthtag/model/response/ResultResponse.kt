package com.flynaut.healthtag.model.response

data class ResultResponse(
    val `data`: Result,
    val message: String,
    val status: Int
)