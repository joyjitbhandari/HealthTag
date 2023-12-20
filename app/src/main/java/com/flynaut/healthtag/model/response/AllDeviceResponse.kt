package com.flynaut.healthtag.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllDeviceResponse(
    val status: Int,
    val `data`: List<AllDevice>) :Parcelable
@Parcelize
data class AllDevice(
    val _id: String,
    val user: String,
    val deviceID: String,
    val name: String,
    val protocolType: String,
    var serialNumber: String,
    val RSSI: String,
    val currentTestType: String,
    val advertisementData: String,
    val nameCached: String,
    val shortName: String,
    val bootID: String,
    val __v: Int,
) : Parcelable
