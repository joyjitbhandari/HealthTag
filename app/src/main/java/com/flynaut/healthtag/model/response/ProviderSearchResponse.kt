package com.flynaut.healthtag.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProviderSearchResponse(

    @field:SerializedName("data")
    val data: List<ProviderSearchDetails>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable

@Parcelize
data class ProviderSearchDetails(

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("specialty")
    val specialty: String? = null,

    @field:SerializedName("tenant")
    val tenant: String? = null
) : Parcelable