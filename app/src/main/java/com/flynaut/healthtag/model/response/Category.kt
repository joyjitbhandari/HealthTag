package com.flynaut.healthtag.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val _id: String,
    val image: String,
    val details: String,
    val name: String
) :Parcelable
