package com.flynaut.healthtag.view.pulseoximeter.utils.ble

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.clj.fastble.scan.BleScanRuleConfig
import com.flynaut.healthtag.databinding.FragmentAddDeciveBinding
import com.flynaut.healthtag.util.OxymeterDeviceList
import com.flynaut.healthtag.view.addDevice.AddDeviceFragment
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceAdapter
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceListDialog
import java.util.*

/*
 * @deprecated Bluetooth
 * @author zl
 * @date 2022/12/2 13:48
 */
class BluetoothManager(
    protected var activity: Activity,
    protected var adapter: DeviceAdapter?,
    protected var mParseRunnable: ParseRunnable?,
    protected var mWaveForm: WaveForm?
) {
    //Initialization configuration
    private fun init() {
        //Init
        BleManager.getInstance().init(activity.application)
        BleManager.getInstance()
            .enableLog(true)
            .setReConnectCount(1, 5000)
            .setConnectOverTime(20000)
            .setMaxConnectCount(1).operateTimeout = 5000
    }

    /**
     * Support Ble
     */
    private val isSupportBle: Boolean
        private get() = BleManager.getInstance().isSupportBle

    /**
     * Determine whether the current Android system supports BLE
     */
    val isBlueEnable: Boolean
        get() = BleManager.getInstance().isBlueEnable

    /**
     * Open Bluetooth
     */
    fun enableBluetooth() {
        BleManager.getInstance().enableBluetooth()
    }

    /**
     * Close Bluetooth
     */
    fun disableBluetooth() {
        BleManager.getInstance().disableBluetooth()
    }

    /**
     * Open
     */
    fun isOpen(dialog: DeviceListDialog,binding : FragmentAddDeciveBinding) {
        try {
            disconnectAllDevice()
            if (!isSupportBle) {
                //ToastUtil.showToastShort("Not Support Ble Device");
                return
            }
            if (!isBlueEnable) {
                enableBluetooth()
            } else {
                dialog.show()
                dialog.dismiss()
                binding?.device2Card!!.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
                enableBluetooth()
            } else {
                disconnectAllDevice()
            }
        }
    }

    //Configuration scan rules
    fun scanRule() {
        val scanRuleConfig = BleScanRuleConfig.Builder()
            .setServiceUuids(null)
            .setDeviceName(true, "")
            .setDeviceMac(null)
            .setAutoConnect(false)
            .setScanTimeOut(10000)
            .build()
        BleManager.getInstance().initScanRule(scanRuleConfig)
    }

    //Scan
    fun scan() {
        Log.e("ScanStaet", "1")
        adapter!!.clear()
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {
                if (adapter != null) {
                    adapter!!.clear()
                    adapter!!.notifyDataSetChanged()
                }
            }

            override fun onScanning(bleDevice: BleDevice) {
                Log.e("scan", "Scan")
                if (adapter != null && bleDevice != null) {

                    adapter!!.addDevice(bleDevice)
                    adapter!!.notifyDataSetChanged()
                    val myDeviceList = AddDeviceFragment()
                    myDeviceList.getDeviceInfo(bleDevice)
                    AddDeviceFragment.getOxyDevice(bleDevice)
                }
            }

            override fun onScanFinished(scanResultList: List<BleDevice>) {
                if (adapter != null) adapter!!.notifyDataSetChanged()
            }
        })
    }

    //Connect with device
    fun conn(bleDevice: BleDevice?) {
        BleManager.getInstance().connect(bleDevice, object : BleGattCallback() {
            override fun onStartConnect() {}
            @RequiresApi(api = Build.VERSION_CODES.R)
            override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int) {
                Log.e("Connected", "Connect Success")
                notification(bleDevice, UUID_SERVICE_DATA, UUID_CHARACTER_RECEIVE)
            }

            override fun onConnectFail(bleDevice: BleDevice, exception: BleException) {
                mHandler.sendEmptyMessage(0x01)
            }

            override fun onDisConnected(
                isActiveDisConnected: Boolean,
                device: BleDevice,
                gatt: BluetoothGatt,
                status: Int
            ) {
                mHandler.sendEmptyMessageDelayed(0x01, 500)
            }
        })
    }

    var mHandler = Handler { msg ->
        if (msg.what == 0x01) {
            if (mWaveForm != null) mWaveForm!!.clear()
        }
        false
    }

    init {
        init()
    }

    /**
     * Disconnect
     */
    fun disconnect(bleDevice: BleDevice?) {
        BleManager.getInstance().disconnect(bleDevice)
    }

    /**
     * Disconnect All Device
     */
    fun disconnectAllDevice() {
        BleManager.getInstance().disconnectAllDevice()
    }

    //Notify
    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    fun notification(
        bleDevice: BleDevice,
        uuid_service: String?,
        uuid_characteristic_notify: String?
    ) {
        val model = toHexString(bleDevice.scanRecord) //device model
        if (DeviceModel.BCI_RR == model) {
            mParseRunnable!!.setModel(DeviceModel.BCI_RR)
        } else {
            mParseRunnable!!.setModel(DeviceModel.BCI_ORDINARY)
        }
        BleManager.getInstance().notify(
            bleDevice,
            uuid_service,
            uuid_characteristic_notify,
            object : BleNotifyCallback() {
                override fun onNotifySuccess() {}
                override fun onNotifyFailure(exception: BleException) {}
                override fun onCharacteristicChanged(data: ByteArray) {
                    try {
                        if (mParseRunnable != null) mParseRunnable!!.add(data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )
    }

    /**
     * model
     *
     *
     * 00:00:00
     */
    private fun toHexString(scanRecord: ByteArray?): String {
        if (scanRecord != null && scanRecord.size > 3) {
            val first = padLeft(Integer.toHexString(scanRecord[scanRecord.size - 3].toInt()))
            val second = padLeft(Integer.toHexString(scanRecord[scanRecord.size - 2].toInt()))
            val third = padLeft(Integer.toHexString(scanRecord[scanRecord.size - 1].toInt()))
            return "$first:$second:$third"
        }
        return "00:00:00"
    }

    private fun padLeft(str: String): String {
        return if (str.length >= 2) str else "0$str"
    }

    companion object {
        val UUID_SERVICE_DATA = UUID.fromString("49535343-fe7d-4ae5-8fa9-9fafd205e455").toString()
        val UUID_CHARACTER_RECEIVE =
            UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616").toString()
        val UUID_MODIFY_BT_NAME = UUID.fromString("49535343-8841-43f4-a8d4-ecbe34729bb3").toString()
    }
}