package com.flynaut.healthtag.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.BottomShareReportBinding
import com.flynaut.healthtag.databinding.DialogAddedToCartDialogBinding
import com.flynaut.healthtag.viewmodel.DeviceInstructionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddedToCartDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding : DialogAddedToCartDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAddedToCartDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.setOnClickListener {
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