package com.flynaut.healthtag.model.response

data class CreateCustomerResponse(
	val `data`: DataCreateCustomer,
	val message: String,
	val status: Int
)

data class DataCreateCustomer(
	val id: String,

)
