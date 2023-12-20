package com.flynaut.healthtag.view.deviceOne

import android.Manifest
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.spirobanksmart_sampleapp.services.AsyncUtility
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentPrepareDeviceBinding
import com.flynaut.healthtag.model.response.AllDevice
import com.flynaut.healthtag.util.BluetoothBroadcastReceiver
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.util.Utils.DEVICE_INFO
import com.flynaut.healthtag.util.Utils.DEVICE_SPIROMETER
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.deviceTwo.Device2StartTestFragment
import com.spirometry.spirobanksmartsdk.Device
import com.spirometry.spirobanksmartsdk.DeviceInfo
import com.spirometry.spirobanksmartsdk.DeviceManager
import com.spirometry.spirobanksmartsdk.DeviceManagerCallback

class PrepareDeviceFragment : BaseFragment<FragmentPrepareDeviceBinding>(), DeviceManagerCallback {
    private var DEVICE_SPIROMETER = true
    private var deviceInfo: AllDevice? = null
    private val deviceList = ArrayList<DeviceInfo>()
    private lateinit var bluetoothBroadcastReceiver: BluetoothBroadcastReceiver
    private var fragmentContext: Context? = null
    var mPermission = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )
    private val REQUEST_CODE_PERMISSION = 3


    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onDetach() {
        super.onDetach()
        fragmentContext = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create and register the BroadcastReceiver
        bluetoothBroadcastReceiver = BluetoothBroadcastReceiver()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        requireContext().registerReceiver(bluetoothBroadcastReceiver, filter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            DEVICE_SPIROMETER = it.getBoolean(DEVICE_SPIROMETER.toString())
            deviceInfo = it.getParcelable(DEVICE_INFO)
            Log.e("DeviceInfo", deviceInfo!!.name + "-" + deviceInfo!!.deviceID)
            setData()
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_prepare_device
    }

    fun setData() {
        deviceManager.addDeviceManagerCallback(this)
        binding.txtDeviceType.setText(deviceInfo?.name.toString())
        binding.txtStartTest.setText("Connecting")
        if (DEVICE_SPIROMETER)
            binding.imgDevice.setImageResource(R.drawable.img_device1)
        else
            binding.imgDevice.setImageResource(R.drawable.img_device2)

        deviceList.clear()
        if (Utils.isBluetooth_enable() && DEVICE_SPIROMETER) {
            deviceManager.startDiscovery(activity)

        } else if (!DEVICE_SPIROMETER) {
            val targetMacAddress = deviceInfo!!.deviceID

            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            val bluetoothDevice: BluetoothDevice? =
                bluetoothAdapter?.getRemoteDevice(targetMacAddress)

            if (bluetoothAdapter == null || bluetoothDevice == null) {
                Toast.makeText(
                    activity,
                    "Please keep the Bluetooth device near Mobile phone",
                    Toast.LENGTH_SHORT
                ).show()
                // Handle case where Bluetooth is not available or device not found
            } else {
                val gattCallback = object : BluetoothGattCallback() {
                    override fun onConnectionStateChange(
                        gatt: BluetoothGatt?,
                        status: Int,
                        newState: Int
                    ) {

                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            (activity as MainActivity).replaceFragment(
                                Device2StartTestFragment(),
                                "Device2StartTestFragment",
                                false
                            )
                            val fragment = Device2StartTestFragment.newInstance(deviceInfo)

                            (activity as MainActivity).replaceFragment(
                                fragment, "PrepareDeviceFragment", false
                            )
                        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                            Log.e("Connetced", "2")
                            Toast.makeText(activity, "Device not connected", Toast.LENGTH_SHORT)
                                .show()
                            // Device disconnected, handle this event
                        }
                    }

                    // Override other BluetoothGattCallback methods here as needed
                }

                try {
                    val bluetoothGatt: BluetoothGatt? =
                        bluetoothDevice.connectGatt(context, false, gattCallback)
                    // Handle the BluetoothGatt instance and its events as needed
                } catch (e: SecurityException) {
                    // Handle the SecurityException here
                }
            }
        } else {
            // Enable Bluetooth
            changeBluetoothState(true)

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister the BroadcastReceiver
        deviceList.clear()
        requireContext().unregisterReceiver(bluetoothBroadcastReceiver)
        deviceManager.disconnect()
    }

    private fun openBluetoothSettings() {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        startActivity(intent)
    }

    private fun changeBluetoothState(enable: Boolean) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null) {
            if (enable) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                bluetoothAdapter.enable()
                setData()

            } else {
                bluetoothAdapter.disable()
            }
        }
    }

    override fun deviceConnectionFailed(deviceInfo: DeviceInfo) {
        super.deviceConnectionFailed(deviceInfo)
        Log.e("Devic", deviceInfo.name)
        binding.txtStartTest.setText("Connection Failed")

        context?.showToast("Device Connnection Failed! Please try again", Toast.LENGTH_SHORT)
    }


    override fun initViews() {


        binding.cardStartTest.setOnClickListener {

            var deviceStatus = binding.txtStartTest.text.toString()
            if (deviceStatus.equals("Connected") && DEVICE_SPIROMETER) {
                (activity as MainActivity).replaceFragment(
                    TestPeakFlowFragment(),
                    "TestPeakFragment",
                    false
                )
            }

//            childFragmentManager.let {
//                bottomSheetDialog.show(
//                    it,
//                    "BottomDeviceInstructionFragment"
//                )
//            }
        }

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    override fun deviceConnected(device: Device) {
        super.deviceConnected(device)
        Log.e("Conneyed", device.deviceInfo.name)
        binding.txtStartTest.setText("Connected")
        binding.txtPeakFlow.visibility = View.VISIBLE
        deviceManager.stopDiscovery()
        (activity as MainActivity).replaceFragment(
            TestPeakFlowFragment(),
            "TestPeakFragment",
            false
        )
    }


    override fun bluetoothPermissionsRequired() {
        super.bluetoothPermissionsRequired()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Missing Android 12 specific Bluetooth permissions //
            DeviceManager
                .getInstance(requireContext())
                .requestBluetoothPermissions(requireActivity(), 1)
        }
    }


    override fun deviceDiscovered(deviceInfo: DeviceInfo) {
        super.deviceDiscovered(deviceInfo)
        Log.e("Disss", "Diss")

        if (deviceInfo.address == deviceInfo.address) {
            deviceList.add(deviceInfo)
        }
        AsyncUtility.doAfterDelay(15000) {
            deviceManager.stopDiscovery()
            if (isResumed) {
                //resetUI()
            }
        }
        fragmentContext?.let { context ->
            Log.e("Dev", "Dev")
            DeviceManager
                .getInstance(fragmentContext)
                .connect(fragmentContext, deviceList.get(0))
        }

    }


    companion object {
        @JvmStatic
        fun newInstance(deviceInfo: AllDevice, device_Spirometer: Boolean): PrepareDeviceFragment {
            val fragment = PrepareDeviceFragment()
            val bundle = Bundle()
            bundle.putBoolean(DEVICE_SPIROMETER, device_Spirometer)
            bundle.putParcelable(DEVICE_INFO, deviceInfo)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        changeBluetoothState(true)
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBluetoothAdapter.isEnabled) {
            Toast.makeText(activity,"Please ON the bluetooth",Toast.LENGTH_SHORT).show()
        }
        if ((ActivityCompat.checkSelfPermission(
                activity!!, mPermission[0]
            ) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(
                activity!!, mPermission[1]
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(activity!!, mPermission, REQUEST_CODE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the requestCode matches the one used while requesting the permission
        // This is useful if you have requested multiple permissions from the same Fragment.
        Log.e("1", requestCode.toString())
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // Missing Android 12 specific Bluetooth permissions //
                    DeviceManager
                        .getInstance(requireContext())
                        .requestBluetoothPermissions(requireActivity(), 1)
//        }
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
    }

}