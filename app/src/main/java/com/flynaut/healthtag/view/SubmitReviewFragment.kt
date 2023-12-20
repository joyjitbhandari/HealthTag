package com.flynaut.healthtag.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentSubmitReviewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SubmitReviewFragment(private val listener: ClickListener) : BottomSheetDialogFragment() {
    interface ClickListener{
        fun onSubmitClick(rating:Int, review:String)
    }

    private lateinit var binding: FragmentSubmitReviewBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etReview.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        binding.btnContinue.setOnClickListener {
            if(binding.etReview.text.isNotEmpty()){
                listener.onSubmitClick(binding.ratingBar.numStars,binding.etReview.text.toString())
                dialog?.dismiss()
            }else{
                Toast.makeText(requireContext(), "Write a review", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSubmitReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)

    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }
}