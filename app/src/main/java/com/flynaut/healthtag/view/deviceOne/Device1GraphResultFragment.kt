package com.flynaut.healthtag.view.deviceOne

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentDevice1GraphResultBinding
import com.flynaut.healthtag.util.Utils.DEVICE_ID
import com.flynaut.healthtag.view.BaseFragment

class Device1GraphResultFragment : BaseFragment<FragmentDevice1GraphResultBinding>() {
    private var selectedButton: Button? = null
//    private var deviceId: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

  /*      arguments?.let {
            deviceId = it.getString(DEVICE_ID).toString()
        }*/

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_device1_graph_result
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

        selectedButton = binding.btnWeek

        binding.btnDay.setOnClickListener { onBtnClicked(binding.btnDay) }
        binding.btnWeek.setOnClickListener { onBtnClicked(binding.btnWeek)}
        binding.btnMonth.setOnClickListener { onBtnClicked(binding.btnMonth) }
        binding.btnYear.setOnClickListener { onBtnClicked(binding.btnYear) }


    }
    private fun onBtnClicked( btnClicked: Button) {
        this.selectedButton?.let { it.setBackgroundResource(R.drawable.rounded_rect_bg_white_gray_border) }
        this.selectedButton?.let { it.setTextColor(ContextCompat.getColor(binding.root.context, R.color._444343)) }

        // Set the background of the selected emoji to the circle drawable
        btnClicked.setBackgroundResource(R.drawable.rounded_rect_bg_red)
        btnClicked.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        this.selectedButton = btnClicked
    }

    companion object {
        @JvmStatic
        fun newInstance(deviceId: String): Device1GraphResultFragment {
            val fragment = Device1GraphResultFragment()
            val bundle = Bundle()
//            bundle.putString(DEVICE_ID, deviceId)
            fragment.arguments = bundle
            return fragment
        }
    }

}