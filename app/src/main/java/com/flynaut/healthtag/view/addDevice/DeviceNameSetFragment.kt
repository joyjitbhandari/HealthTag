package com.flynaut.healthtag.view.addDevice

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.clj.fastble.data.BleDevice
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentDeviceNameSetBinding
import com.flynaut.healthtag.model.OxyDeviceItem
import com.flynaut.healthtag.model.request.EditDeviceRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.view.*
import com.flynaut.healthtag.viewmodel.AddDeviceViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.spirometry.spirobanksmartsdk.DeviceInfo


class DeviceNameSetFragment : BaseFragment<FragmentDeviceNameSetBinding>(){

    private lateinit var viewModel : AddDeviceViewModel
    private var deviceList = ArrayList<DeviceInfo>()
    private var _id  = ""
    private var name  = ""
    private var isEdit = false
    private var oxyDevice = ArrayList<OxyDeviceItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //for shared animation
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_trans)
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[AddDeviceViewModel::class.java]

        arguments?.let {
            if (it.getBoolean(Utils.DEVICE_EDIT) == true)
            {
                isEdit = true
                _id = it.getString(Utils.DEVICE_ID).toString()
                name = it.getString(Utils.DEVICE_NAME).toString()

            }else{
                isEdit = false
                deviceList = it.getSerializable(Utils.DEVICE_INFO) as ArrayList<DeviceInfo>
                oxyDevice = it.getSerializable(Utils.OXY_DEVICE) as ArrayList<OxyDeviceItem> // Retrieve oxyDevice from arguments
                Log.e("oxyDevice in fragment", oxyDevice?.get(0)!!.mac.toString())

            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_device_name_set
    }

    override fun initViews() {
        binding.btnFinish.setOnClickListener {
            showProgressDialog()

            var name = binding.etNickName.text.toString().trim()

            showProgressDialog()
            var advertisement = ""

            if (!isEdit) {
                if (deviceList.size > 0){
                    var list = deviceList.get(0)

                    advertisement = Utils.SPIROMETER
                viewModel.addDevice(
                    list.address.toString(),
                    name,
                    list.protocol.toString(),
                    list.serialNumber.toString(),
                    "",
                    "",
                    advertisement,
                    "",
                    "",
                    list.bootId.toString()
                )
            } else {
                advertisement = Utils.OXYMETER
                    var list = oxyDevice.get(0)

                    viewModel.addDevice(
                    list.mac,
                    name,
                    "",
                    "",
                        list.rssi.toString(),
                    "",
                    advertisement,
                    "",
                    "",
                    ""
                )
            }
        }
            else {
                val editDeviceRequest = EditDeviceRequest(name)
                viewModel.updateDevice(_id,editDeviceRequest)
            }

        }
    }

    private fun initObserver() {
        if (deviceList.size > 0 && !isEdit)
        {
            var name = deviceList.get(0).name
            binding.etNickName.setText(name)
            //binding.txtNickName.setText(name)
        }
        else if (deviceList.size == 0 && !isEdit)
        {
            var name = oxyDevice.get(0).name
            binding.etNickName.setText(name)
        }
        else{
            binding.etNickName.setText(name)
            binding.txtNickName.setText(name)
        }

        viewModel.apiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            if (it.status.equals(200)){

                val fragment = DeviceConnectedFragment.newInstance()
                (activity as MainActivity).replaceFragment(
                    fragment, "DeviceConnectedFragment",false)
            }else{
                Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.updateDeviceResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            Log.e("Stata",it.status.toString())
            if (it.status.equals(200)){
                Log.e("Stata","11")
                val fragment = MyDeviceFragment()
                val bundle = Bundle()
                fragment.arguments = bundle
                (activity as MainActivity).replaceFragment(fragment,"MyDeviceFragment")
            }
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(
            deviceInfo: ArrayList<DeviceInfo>,
            oxyDevice: ArrayList<OxyDeviceItem>, isEdit: Boolean): DeviceNameSetFragment {
            val fragment = DeviceNameSetFragment()
            val bundle = Bundle()
            bundle.putBoolean(Utils.DEVICE_EDIT, isEdit)
            bundle.putSerializable(Utils.DEVICE_INFO, deviceInfo)
            bundle.putSerializable(Utils.OXY_DEVICE, oxyDevice)
            fragment.arguments = bundle
            Log.e("spiro",deviceInfo.size.toString())
            Log.e("oxyName",oxyDevice?.get(0)!!.toString())
            return fragment
        }
    }
}