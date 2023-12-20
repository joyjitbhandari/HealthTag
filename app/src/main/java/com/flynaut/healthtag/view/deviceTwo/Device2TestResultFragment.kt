package com.flynaut.healthtag.view.deviceTwo

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentDevice2TestResultBinding
import com.flynaut.healthtag.util.Utils.DEVICE2
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.deviceOne.AllResultListFragment

class Device2TestResultFragment : BaseFragment<FragmentDevice2TestResultBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isFirstTime = true

        binding.btnViewResult.setOnClickListener {
            if(isFirstTime){

                //set text
                binding.txtHeartRateReport.text = "55"
                binding.txtSpo2Report.text = "81"

                //set image
                binding.resultImage.setImageResource(R.drawable.img_sad_heart)

                //set mode
                binding.cardModeThird.setCardBackgroundColor(resources.getColor(R.color.white))
                binding.ivModeThird.setImageResource(R.drawable.img_mode_third)

                binding.cardModeFirst.setCardBackgroundColor(resources.getColor(R.color.green))
                binding.ivModeFirst.setImageResource(R.drawable.img_white_mode_first)

            }else{
                //i have to provide device id and make changes here
                val fragment = AllResultListFragment.newInstance(DEVICE2)
                (activity as MainActivity).replaceFragment(fragment,"AllResultListFragment",false)
            }
            isFirstTime = !isFirstTime
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_device2_test_result
    }

    override fun initViews() {

        binding.etNote.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Device2TestResultFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}