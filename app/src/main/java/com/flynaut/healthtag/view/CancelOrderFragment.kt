package com.flynaut.healthtag.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentCancelOrderBinding
import com.flynaut.healthtag.databinding.FragmentCreateTicketBinding
import com.flynaut.healthtag.model.response.DataItemOrderDetail
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.MyOrderViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CancelOrderFragment(val listener : OnButtonClick) : BottomSheetDialogFragment() {

    interface OnButtonClick{
        fun onYesClicked()
    }
    private lateinit var binding: FragmentCancelOrderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCancelOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnYes.setOnClickListener {
            listener.onYesClicked()
            dialog?.dismiss()
        }

        binding.btnNo.setOnClickListener {
            dialog?.dismiss()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }
}