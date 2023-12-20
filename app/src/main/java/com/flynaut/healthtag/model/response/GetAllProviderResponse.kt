package com.flynaut.healthtag.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetAllProviderResponse(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItemProvider>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataItemProvider(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("specialty")
	val specialty: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("tenant")
	val tenant: String? = null
) : Parcelable
