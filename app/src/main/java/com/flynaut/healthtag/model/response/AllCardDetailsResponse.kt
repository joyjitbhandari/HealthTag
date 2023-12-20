package com.flynaut.healthtag.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllCardDetailsResponse(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("data")
	val data: List<CardDetails>? = null,

	@field:SerializedName("message")
	val message: String? = null,
) : Parcelable

@Parcelize
data class CardDetails(

	@field:SerializedName("address_zip_check")
	val addressZipCheck: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("last4")
	val last4: String? = null,

	@field:SerializedName("funding")
	val funding: String? = null,

	@field:SerializedName("wallet")
	val wallet: String? = null,

	@field:SerializedName("address_country")
	val addressCountry: String? = null,

	@field:SerializedName("address_state")
	val addressState: String? = null,

	@field:SerializedName("exp_month")
	val expMonth: Int? = null,

	@field:SerializedName("exp_year")
	val expYear: Int? = null,

	@field:SerializedName("address_city")
	val addressCity: String? = null,

	@field:SerializedName("tokenization_method")
	val tokenizationMethod: String? = null,

	@field:SerializedName("cvc_check")
	val cvcCheck: String? = null,

	@field:SerializedName("address_line2")
	val addressLine2: String? = null,

	@field:SerializedName("address_line1")
	val addressLine1: String? = null,

	@field:SerializedName("fingerprint")
	val fingerprint: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("address_line1_check")
	val addressLine1Check: String? = null,

	@field:SerializedName("address_zip")
	val addressZip: String? = null,

	@field:SerializedName("dynamic_last4")
	val dynamicLast4: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("customer")
	val customer: String? = null
) : Parcelable
