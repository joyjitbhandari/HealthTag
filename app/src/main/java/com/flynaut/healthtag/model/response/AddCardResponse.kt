package com.flynaut.healthtag.model.response

data class AddCardResponse(
    val status: Int,
    val message: String,
    val `data`: CardDetails,
)
