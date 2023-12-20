package com.flynaut.healthtag.model

data class Add_DeviceResponse(
    val status: Int,
    val message: String,
    val error : String,
    val `data`: AddDeviceData)

data class AddDeviceData(
    val deviceID: String,
    val name: String,
    val protocolType: String?,
    val serialNumber: String,
    val RSSI: String,
    val currentTestType: String,
    val advertisementData: String,
    val nameCached: String,
    val shortName: String,
    val bootID: String,
)