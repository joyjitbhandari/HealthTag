package com.flynaut.healthtag.model

data class OxyDeviceItem(
    var name:String, var mac: String, var rssi: Int,
    var scanRecord: ByteArray, var timeStampNan: Long
) {}