package com.flynaut.healthtag.view.deviceTwo

import android.os.Bundle
import android.view.View
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentTouchToStartBinding
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.deviceOne.TestPeakFlowResultFragment

class TouchToStartFragment : BaseFragment<FragmentTouchToStartBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_touch_to_start
    }

    override fun initViews() {
        var isFirstTime = true
        binding.touchBody.setOnClickListener {
            if(isFirstTime){
                binding.cardTouch.setCardBackgroundColor(resources.getColor(R.color._4FA841))
                binding.ivTouchPrint.setImageResource(R.drawable.ic_green_finger_print)
                binding.txtBottom.text = resources.getString(R.string.nice_starting)
            }else{
                (activity as MainActivity).replaceFragment(Device2StartTestFragment(), "Device2StartTestFragment",false)
            }

            isFirstTime = !isFirstTime
        }


        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestPeakFlowResultFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}