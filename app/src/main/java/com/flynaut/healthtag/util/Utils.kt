package com.flynaut.healthtag.util

import android.bluetooth.BluetoothAdapter
import android.os.Build
import android.os.Bundle
import com.flynaut.healthtag.model.DeviceListItem
import com.flynaut.healthtag.model.response.UserData
import com.flynaut.healthtag.model.response.UserProfile
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.TimeZone
import kotlin.reflect.KClass

object Utils {
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isBluetooth_enable() : Boolean {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!mBluetoothAdapter.isEnabled)
            return false
        else
            return true
    }

    public fun extractTime(timestamp: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val date = sdf.parse(timestamp)
            SimpleDateFormat("hh:mm aaa").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            null

        }
    }
    inline fun <reified T : Any> KClass<T>.getParcelable(bundle: Bundle, key: String): T? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            bundle.getParcelable(key, T::class.java)
        else
            bundle.getParcelable(key)


    //Device cardDetails store
    val deviceDataList: ArrayList<DeviceListItem> = ArrayList()

    const val DEVICE1 = "100"
    const val DEVICE2 = "200"

    //bundle key
    const val DEVICE_ID = "deviceId"
    const val DEVICE_NAME = "deviceName"
    const val DEVICE_INFO = "deviceInfo"
    const val OXY_DEVICE = "BleDevice"
    const val DEVICE_EDIT = "deviceEdit"
    const val DEVICE_SPIROMETER = "DEVICE"
    const val CARD_ID = "card_id"
    const val POSITION = "position"
    const val SPIROMETER = "Spirometer"
    const val OXYMETER = "oxymeter"
    var ISHOME = true


}
