package com.flynaut.healthtag.util

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Check the action of the received intent
        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

            // Check the different states
            when (state) {
                BluetoothAdapter.STATE_OFF -> {
                    // Bluetooth is turned off
                }
                BluetoothAdapter.STATE_TURNING_OFF -> {
                    // Bluetooth is turning off
                }
                BluetoothAdapter.STATE_ON -> {
                    // Bluetooth is turned on
                }
                BluetoothAdapter.STATE_TURNING_ON -> {
                    // Bluetooth is turning on
                }
            }
        }
    }
}