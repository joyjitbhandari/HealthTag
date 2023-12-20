package com.flynaut.healthtag.model.response

data class LogsListResponse(
    val `data`: List<LogsData>,
    val message: String
)

data class LogsData(
    val _id: String,
    val deviceName: String,
    val deviceImage: String,
    val status: String,
    val date: String,
    val createdAt: Double,
    var updatedAt: Int,
    val __v: String
)
