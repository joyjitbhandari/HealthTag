package com.flynaut.healthtag.util

import com.clj.fastble.data.BleDevice
 interface OxymeterDeviceList
{
    fun getDeviceInfo(devices: BleDevice) {}
}