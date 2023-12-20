package com.flynaut.healthtag.model.response

data class ErrorResponse(
    val name: String,
    val errors: String,
    val message: String
)
