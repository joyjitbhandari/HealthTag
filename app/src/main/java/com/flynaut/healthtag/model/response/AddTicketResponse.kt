package com.flynaut.healthtag.model.response

data class AddTicketResponse(
    val `data`: AddTicketData,
    val message: String,
    val status: Int
)

data class AddTicketData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val status: Int,
    val ticketId: Int,
    val ticketTitle: String,
    val topic: String,
    val updatedAt: String,
    val user: String
)