package com.flynaut.healthtag.model.response

import com.google.gson.annotations.SerializedName

data class LogsListDetailsResponse(

    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val `data`: List<Day>
)

data class Day(
    @field:SerializedName("day")
    val day: String? = null,
    @field:SerializedName("logs")
    val logs: List<Logs>? = null
    )

data class Logs(
    @field:SerializedName("_id")
    val _id: String? = null,
    @field:SerializedName("deviceName")
    val deviceName: String? = null,
    @field:SerializedName("deviceImage")
    val deviceImage: String? = null,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("date")
    val date: String,
    @field:SerializedName("createdAt")
    val createdAt: String? = null,
    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,
    @field:SerializedName("__v")
    val __v: String? = null,
    @field:SerializedName("day")
    val day: String? = null,
)