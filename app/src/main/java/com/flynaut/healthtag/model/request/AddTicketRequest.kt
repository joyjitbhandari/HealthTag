package com.flynaut.healthtag.model.request

data class AddTicketRequest(
    val ticketTitle: String,
    val description: String,
    val topic: String
)

data class SearchFaq(
    val search: String
)
