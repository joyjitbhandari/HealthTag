package com.flynaut.healthtag.view.addDevice

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentDeviceConnectingBinding
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MyDeviceFragment

class DeviceConnectingFragment : BaseFragment<FragmentDeviceConnectingBinding>(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            val parentFragment = parentFragment as? MyDeviceFragment
            parentFragment?.addFragmentsToMyDeviceParentFragment(DeviceNameSetFragment(),"DeviceNameSetFragment")
        }, 3000)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_device_connecting
    }

    override fun initViews() {


    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeviceConnectingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}