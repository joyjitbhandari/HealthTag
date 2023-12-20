package com.flynaut.healthtag.view.deviceOne

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.BottomDeviceInstructionBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.CustomTypefaceSpan
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.Utils.DEVICE1
import com.flynaut.healthtag.util.Utils.DEVICE2
import com.flynaut.healthtag.util.Utils.DEVICE_ID
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.view.BaseBottomSheetFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.deviceTwo.TouchToStartFragment
import com.flynaut.healthtag.viewmodel.DeviceInstructionViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class BottomDeviceInstructionFragment : BaseBottomSheetFragment<BottomDeviceInstructionBinding>() {
    private var deviceId: String? = null
    private lateinit var viewModel: DeviceInstructionViewModel
    override val layoutResourceId: Int
        get() = R.layout.bottom_device_instruction

    override fun initViews() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[DeviceInstructionViewModel::class.java]
        initObserver()
        initData()
        setClick()
    }

    private fun initObserver() {
        viewModel.finalDeviceInstResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            if (it.data == null) {
                //binding.txtNoData.visibility = View.VISIBLE
                //binding.rvOrder.visibility = View.GONE
            } else {
                //binding.txtNoData.visibility = View.GONE
                //binding.rvOrder.visibility = View.VISIBLE
                viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
                    hideProgressDialog()
                    context?.showToast(it)
                })
            }
        }
    }

    private fun initData() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[DeviceInstructionViewModel::class.java]
        showProgressDialog()
        viewModel.getDeviceInstruction()
        setCustomTextHeading()
    }


    private fun setClick() {
        binding.btnOk.setOnClickListener {
            arguments?.let {
                deviceId = it.getString(DEVICE_ID)
            }

            when (deviceId) {
                DEVICE1 -> {
                    (activity as MainActivity).replaceFragment(
                        TestPeakFlowFragment(),
                        "TestPeakFragment",
                        false
                    )
                    this.dismiss()
                }

                DEVICE2 -> {
                    (activity as MainActivity).replaceFragment(
                        TouchToStartFragment(),
                        "TouchToStartFragment",
                        false
                    )
                    this.dismiss()
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(deviceId: String): BottomDeviceInstructionFragment {
            val fragment = BottomDeviceInstructionFragment()
            val bundle = Bundle()
            bundle.putString(DEVICE_ID, deviceId)
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun setCustomTextHeading() {

        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Device")
        val firstFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular) })
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            firstText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val secondText = SpannableString("Instruction")
        val secondFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_semibold) })
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.root.context.let {
            secondText.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        it,
                        R.color.black
                    )
                ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        builder.append(firstText)
        builder.append(" ")
        builder.append(secondText)

        binding.tvHeading.text = builder
    }
}