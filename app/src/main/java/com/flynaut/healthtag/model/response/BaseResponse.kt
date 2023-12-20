package com.flynaut.healthtag.model.response

data class BaseResponse(
    val status: Boolean,
    val message: String,
    val data: Any?
)