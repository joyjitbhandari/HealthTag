package com.flynaut.healthtag.view.deviceOne

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.BottomShareReportBinding
import com.flynaut.healthtag.util.CustomTypefaceSpan
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class BottomShareReportFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomShareReportBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCustomTextHeading()

        val providers = listOf("Provider1", "Provider2", "Provider3")
        val adapter = context?.let { ArrayAdapter(it, R.layout.spinner_dropdown_item, providers) }
        adapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spProvider.adapter = adapter

        binding.etProviderType.setOnClickListener {
            binding.spProvider.performClick()
        }
        binding.spProvider.setSelection(1,false)

        binding.spProvider.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedProvider = parent.getItemAtPosition(position).toString()
                binding.etProviderType.text = selectedProvider
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.etStartDate.setOnClickListener{
            showDatePickerDialog(binding.etStartDate)
        }

        binding.etEndDate.setOnClickListener{
            showDatePickerDialog(binding.etEndDate)
        }

        binding.btnSubmit.setOnClickListener {


        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomShareReportBinding.inflate(inflater, container, false)
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

        val firstText = SpannableString("Share")
        val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular) })
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(ForegroundColorSpan(Color.BLACK), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString("Report")
        val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it,R.font.outfit_semibold)})
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.root.context.let { secondText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.black) ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

        builder.append(firstText)
        builder.append(" ")
        builder.append(secondText)

        binding.tvHeading.text = builder
    }

    private fun showDatePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),R.style.RedDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
               textView.text = "$dayOfMonth/$monthOfYear/$year"
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

}