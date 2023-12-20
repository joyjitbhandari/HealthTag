package com.flynaut.healthtag.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FaqCategoryResponse(

	@field:SerializedName("data")
	val data: List<DataItemFaq>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class DataItemFaq(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("faqsCount")
	val faqsCount: Int? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("categoryName")
	val categoryName: String? = null
) : Parcelable
