package com.flynaut.healthtag.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryDetailResponse(
    val `data`: Category,
    val message: String,
    val status: Int
) :Parcelable
