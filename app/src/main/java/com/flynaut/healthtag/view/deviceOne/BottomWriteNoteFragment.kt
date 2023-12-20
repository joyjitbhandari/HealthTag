package com.flynaut.healthtag.view.deviceOne

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.BottomWriteNoteBinding
import com.flynaut.healthtag.util.CustomTypefaceSpan
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomWriteNoteFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomWriteNoteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomTextHeading()

        binding.etNote.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        binding.btnSubmit.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomWriteNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }


    private fun setCustomTextHeading() {
        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Write a ")
        val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular) })
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(ForegroundColorSpan(Color.BLACK), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString("Note")
        val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it,R.font.outfit_semibold)})
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.root.context.let { secondText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.black) ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

        builder.append(firstText)
        builder.append(" ")
        builder.append(secondText)

        binding.tvHeading.text = builder
    }
}