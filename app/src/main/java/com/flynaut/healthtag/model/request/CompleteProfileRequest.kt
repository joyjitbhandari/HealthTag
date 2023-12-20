package com.flynaut.healthtag.model.request

import android.os.Parcelable
import com.stripe.android.model.DateOfBirth
import kotlinx.android.parcel.Parcelize
import java.io.File

data class CompleteProfileRequest(
    val firstName: String,
    val lastName: String,
    val gender: String,
    val height: Int,
    val weight: Int,
    val languagePreference: String,
    val profilePicture : String,
    val phoneNumber : Long,
    val race : String ,
    val dateOfBirth: String
)
