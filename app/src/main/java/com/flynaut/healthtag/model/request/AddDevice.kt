package com.flynaut.healthtag.model.request

data class AddDevice (
val deviceID: String,
val name: String,
val protocolType: String?,
val serialNumber: String,
val RSSI: String,
val currentTestType: String,
val advertisementData: String,
val nameCached: String,
val shortName: String,
val bootID: String)