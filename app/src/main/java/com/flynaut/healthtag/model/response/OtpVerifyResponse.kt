package com.flynaut.healthtag.model.response

data class OtpVerifyResponse(
    val OTP: String,
    val `data`: Data,
    val message: String,
    val status: Int
)