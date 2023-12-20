package com.flynaut.healthtag.view.deviceTwo

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.clj.fastble.data.BleDevice
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentStartTestBinding
import com.flynaut.healthtag.model.response.AllDevice
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.BluetoothManager
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.ParseRunnable
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.ParseRunnable.OnDataChangeListener
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.WaveForm
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceAdapter
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceListDialog
import com.spirometry.spirobanksmartsdk.DeviceManager


class Device2StartTestFragment : BaseFragment<FragmentStartTestBinding>() {

    private var ble: BluetoothManager? = null
    private var dialog: DeviceListDialog? = null
    private var deviceInfo: AllDevice? = null

    private var mParseRunnable: ParseRunnable? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            deviceInfo = it.getParcelable(Utils.DEVICE_INFO)
            Log.e("DeviceInfo",deviceInfo!!.name+"-"+deviceInfo!!.deviceID)
            var bles = createByMac(deviceInfo!!.deviceID)

            if (bles != null) {
                ble!!.conn(bles)
            }

        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_start_test
    }

    override fun initViews() {
        binding.wave!!.setWaveformVisibility(true)
        _runnable()
        val adapter = DeviceAdapter(activity)
        Thread(mParseRunnable).start()
        ble = activity?.let { BluetoothManager(it, adapter, mParseRunnable, binding.wave) }
        dialog = DeviceListDialog(activity, ble, adapter)

        var isFirstTime = true
        binding.cardProgress.setOnClickListener {
            if(isFirstTime){
                binding.progress.setIndicatorColor(resources.getColor(R.color.red))
                binding.progress.setProgress(70,true)
                binding.txtProgress.text = "70%"

                binding.pulseView.visibility = View.GONE
                binding.ivPulseOngoing.visibility = View.VISIBLE
            }else{
                (activity as MainActivity).replaceFragment(Device2TestResultFragment(), "Device2TestResultFragment",false)
            }

            isFirstTime = !isFirstTime

        }

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun _runnable() {
        Log.e("Runnable","Runnagble")
        mParseRunnable = ParseRunnable(object : OnDataChangeListener {
            override fun spo2Val(spo2: Int) {
                Log.e("spo2Val",spo2.toString())
                activity!!.runOnUiThread { binding.txtSpo2Report.setText(if (spo2 > 0) spo2.toString() + "" else "--") }

            }

            override fun prVal(pr: Int) {
                Log.e("pr",pr.toString())

                activity!!.runOnUiThread { binding.txtHeartRateReport.setText(if (pr > 0) pr.toString() + "" else "--") }
            }

            override fun waveVal(wave: Int) {
                activity!!.runOnUiThread { binding.wave!!.addAmplitude(wave) }
            }

            override fun piVal(pi: Double) {
                Log.e("pi",pi.toString())
                //activity!!.runOnUiThread {  binding.txtHeartRateReport.setText(if (pi > 0) pi.toString() + "" else "--") }
            }

            override fun rrVal(rr: Int) {
                Log.e("rr",rr.toString())
                //activity!!.runOnUiThread { mRR.setText(if (rr > 0) rr.toString() + "" else "--") }
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(allDevice: AllDevice?) : Device2StartTestFragment{
            val fragment = Device2StartTestFragment()
            val bundle = Bundle()
            bundle.putParcelable(Utils.DEVICE_INFO, allDevice)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mParseRunnable!!.setStop(false)

    }

    fun createByMac(macAddress: String?): BleDevice? {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            ?: return null // Bluetooth not supported on this device
        val bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress)
            ?: return null // Device with the given MAC address not found
        return BleDevice(bluetoothDevice)
    }




}