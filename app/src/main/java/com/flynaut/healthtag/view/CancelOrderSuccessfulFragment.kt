package com.flynaut.healthtag.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentCancelOrderSuccessfulBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CancelOrderSuccessfulFragment() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCancelOrderSuccessfulBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCancelOrderSuccessfulBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {
            (activity as MainActivity).replaceFragment(DashboardFragment(),"DashboardFragment")
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