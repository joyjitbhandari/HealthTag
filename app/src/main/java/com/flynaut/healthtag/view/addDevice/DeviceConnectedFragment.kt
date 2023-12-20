package com.flynaut.healthtag.view.addDevice

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentDeviceConnectedBinding
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.MyDeviceFragment

class DeviceConnectedFragment : BaseFragment<FragmentDeviceConnectedBinding>(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            val fragment = MyDeviceFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            (activity as MainActivity).replaceFragment(fragment,"MyDeviceFragment",false)
        }, 2000)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_device_connected
    }

    override fun initViews() {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DeviceConnectedFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}