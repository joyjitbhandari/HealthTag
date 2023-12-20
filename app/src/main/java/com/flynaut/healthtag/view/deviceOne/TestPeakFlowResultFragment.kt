package com.flynaut.healthtag.view.deviceOne

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentTestPeakFlowResultBinding
import com.flynaut.healthtag.util.Utils.DEVICE1
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity


class TestPeakFlowResultFragment : BaseFragment<FragmentTestPeakFlowResultBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var click = false

        binding.btnViewResult.setOnClickListener {
            if(click){
                val fragment = AllResultListFragment.newInstance(DEVICE1)
                (activity as MainActivity).replaceFragment(fragment, "AllResultListFragment",false)

            }else{
                binding.cardBg.setBackgroundResource(R.drawable.red_btn_bg)
                binding.txtReport.text = "445"
                binding.resultImage.setImageResource(R.drawable.img_bad_score)
            }
            click = !click
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_test_peak_flow_result
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
            TestPeakFlowResultFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}