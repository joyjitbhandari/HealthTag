package com.flynaut.healthtag.view.addDevice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.clj.fastble.data.BleDevice
import com.example.spirobanksmart_sampleapp.services.AsyncUtility
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentAddDeciveBinding
import com.flynaut.healthtag.model.OxyDeviceItem
import com.flynaut.healthtag.util.OxymeterDeviceList
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.BluetoothManager
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.ParseRunnable
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.ParseRunnable.OnDataChangeListener
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.WaveForm
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceAdapter
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceListDialog
import com.flynaut.healthtag.view.pulseoximeter.utils.per.Permissions
import com.spirometry.spirobanksmartsdk.Device
import com.spirometry.spirobanksmartsdk.DeviceInfo
import com.spirometry.spirobanksmartsdk.DeviceManager
import com.spirometry.spirobanksmartsdk.DeviceManagerCallback


class AddDeviceFragment : BaseFragment<FragmentAddDeciveBinding>(), DeviceManagerCallback,View.OnClickListener, OxymeterDeviceList  {

    private val deviceList = ArrayList<DeviceInfo>()
    lateinit var mainHandler: Handler

    var mPermission = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )
    private val REQUEST_CODE_PERMISSION = 3

    var stop = false

    private val mWaveForm: WaveForm? = null
    private var ble: BluetoothManager? = null
    private var dialog: DeviceListDialog? = null
    private var mParseRunnable: ParseRunnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_add_decive
    }

    override fun initViews() {
        deviceManager.addDeviceManagerCallback(this)

        //transitionDrawable = (TransitionDrawable)ContextCompat!!.getDrawable(context, R.drawable.transition);
        binding.device1Card.setOnClickListener(this)
        binding.bluetoothIcon.setOnClickListener(this)
        binding.tvNeedHelp.setOnClickListener(this)
        binding.device2Card.setOnClickListener(this)

        oxyDevice.clear()
    }

    fun oxymeterViews()
    {
        _runnable()
        val adapter = DeviceAdapter(activity)
        Thread(mParseRunnable).start()
        ble = activity?.let { BluetoothManager(it, adapter, mParseRunnable, mWaveForm) }
        dialog = DeviceListDialog(activity, ble, adapter)
        ble!!.scanRule()
        Permissions.all(activity, ble, dialog,binding)
    }


    private fun bluetoothAnimation(resource: Int) {
        if (isAdded) {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in)
            binding.roundedBackgroundImageView.startAnimation(animation)
            binding.roundedBackgroundImageView.setImageResource(resource)
        }
    }


    private fun startDiscovery() {

        deviceList.clear()
        mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                if (!stop) {
                    bluetoothAnimation(R.drawable.bluetooth_background_blink)
                    mainHandler.postDelayed(this, 1000)
                }

            }
        })

        deviceManager.startDiscovery(activity)
    }


    override fun deviceConnectionFailed(deviceInfo: DeviceInfo) {
        super.deviceConnectionFailed(deviceInfo)

        context?.showToast("Device Connnection Failed! Please try again", Toast.LENGTH_SHORT)
    }


    override fun bluetoothPermissionsRequired() {
        super.bluetoothPermissionsRequired()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            // Missing Android 12 specific Bluetooth permissions //
//            DeviceManager
//                .getInstance(requireContext())
//                .requestBluetoothPermissions(requireActivity(), 1)
//        }
    }

    override fun deviceDiscovered(deviceInfo: DeviceInfo) {
        super.deviceDiscovered(deviceInfo)
        binding.device1Card!!.visibility = View.VISIBLE
        stop = true
        bluetoothAnimation(R.drawable.circle_red_without_stroke)
        Log.e("Dee",deviceInfo.name)
        Log.e("address",deviceInfo.address)
        if (!deviceList.any { it.address == deviceInfo.address }) {
            deviceList.add(deviceInfo)
            oxyDevice.clear()
//            val fragment = DeviceNameSetFragment.newInstance(deviceList, oxyDevice,false)
//
//            (activity as MainActivity).replaceFragment(
//                fragment, "DeviceNameSetFragment"
//            )
        }

        AsyncUtility.doAfterDelay(15000) {
            deviceManager.stopDiscovery()
            if (isResumed) {
                //resetUI()
            }
        }
    }

    override fun deviceConnected(device: Device) {
        super.deviceConnected(device)
        hideProgressDialog()
//        val fragment = DeviceNameSetFragment.newInstance(deviceList, false)
//
//        (activity as MainActivity).replaceFragment(
//            fragment, "DeviceNameSetFragment"
//        )

//        val fragment = DeviceNameSetFragment()
//        val bundle = Bundle()
//        bundle.putSerializable(Utils.DEVICE_INFO, deviceList)
//        bundle.putBoolean(Utils.DEVICE_EDIT, false)
//        fragment.arguments = bundle
//        (activity as MainActivity).replaceFragment(fragment,"DeviceNameSetFragment")
    }

    override fun locationEnabledRequired() {
        super.locationEnabledRequired()
        showDialog("Must enable location first") {
            deviceManager.openLocationSettings(activity)
            //resetUI()
        }
    }

    override fun accessFineLocationPermissionRequired() {
        super.locationEnabledRequired()
        showDialog("Must enable Fine Location permission first") {
            deviceManager.requestFineLocationPermission(activity, 1)
            //resetUI()
        }
    }

    override fun bluetoothIsPoweredOff() {
        super.bluetoothIsPoweredOff()
        showDialog("Must enable Bluetooth first") {
            deviceManager.turnOnBluetooth(activity)
            //resetUI()
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.bluetooth_icon -> {
                startSearchingBluetoothDevice()
            }
            R.id.device1Card -> {
                showProgressDialog()
                DeviceManager
                    .getInstance(requireActivity())
                    .connect(requireActivity(), deviceList.get(0))
            }
            R.id.device2Card -> {
                deviceList.clear()
                Log.e("O1",oxyDevice.size.toString())
                val fragment = DeviceNameSetFragment.newInstance(deviceList,oxyDevice, false)

                (activity as MainActivity).replaceFragment(
                    fragment, "DeviceNameSetFragment"
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stop = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the requestCode matches the one used while requesting the permission
        // This is useful if you have requested multiple permissions from the same Fragment.
        Log.e("1", requestCode.toString())
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(activity, "Please allow permissions", Toast.LENGTH_SHORT).show()
                if ((ActivityCompat.checkSelfPermission(activity!!, mPermission[0])
                            != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(
                        activity!!, mPermission[1]
                    ) != PackageManager.PERMISSION_GRANTED)
                ) {
                    ActivityCompat.requestPermissions(
                        activity!!, mPermission, REQUEST_CODE_PERMISSION
                    )
                }
            }
        }

    }

    fun startSearchingBluetoothDevice() {
        if (Utils.isBluetooth_enable()) {
            startDiscovery() // 4 spirometer
            oxymeterViews()
            Log.e("searc","1");

        } else {
            showDialog("Must enable Bluetooth first") {
                deviceManager.turnOnBluetooth(activity)
                //resetUI()
            }
        }
    }

    private fun _runnable() {
        mParseRunnable = ParseRunnable(object : OnDataChangeListener {
            override fun spo2Val(spo2: Int) {
            Log.e("spo2Val",spo2.toString())
            //activity!!.runOnUiThread { mSpo2.setText(if (spo2 > 0) spo2.toString() + "" else "--") }

            }

            override fun prVal(pr: Int) {
                Log.e("pr",pr.toString())

                //activity!!.runOnUiThread { mPr.setText(if (pr > 0) pr.toString() + "" else "--") }
            }

            override fun waveVal(wave: Int) {
                activity!!.runOnUiThread { mWaveForm!!.addAmplitude(wave) }
            }

            override fun piVal(pi: Double) {
                Log.e("pi",pi.toString())

                //activity!!.runOnUiThread { mPi.setText(if (pi > 0) pi.toString() + "" else "--") }
            }

            override fun rrVal(rr: Int) {
                Log.e("rr",rr.toString())
                //activity!!.runOnUiThread { mRR.setText(if (rr > 0) rr.toString() + "" else "--") }
            }
        })
    }


    override fun onResume() {
        super.onResume()

        if ((ActivityCompat.checkSelfPermission(
                activity!!, mPermission[0]) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(
                activity!!, mPermission[1]) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(activity!!, mPermission, REQUEST_CODE_PERMISSION)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mParseRunnable!!.setStop(false)
    }

    override fun getDeviceInfo(device: BleDevice) {
        // Implement your logic here
        println("Device Name111: ${device.name}, Device Address: ${device.mac},Device RSsI:${device.rssi}")

    }

    companion object{
        private var oxyDevice = ArrayList<OxyDeviceItem>()

        @JvmStatic
        fun getOxyDevice(device : BleDevice)
        {
            var name = device.name ?: "Oxymeter"
            Log.e("Name",name)
            oxyDevice.add(OxyDeviceItem(name,device.mac,device.rssi,device.scanRecord,device.timestampNanos))
            Log.e("OSs",oxyDevice.get(0).name)
        }
    }
}