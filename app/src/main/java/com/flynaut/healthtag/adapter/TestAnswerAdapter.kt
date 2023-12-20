package com.flynaut.healthtag.adapter

import android.content.res.ColorStateList
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterTestAnswerBinding
import com.flynaut.healthtag.model.TestQuestion
import kotlin.math.roundToInt


class TestAnswerAdapter(private val items: List<TestQuestion>) : RecyclerView.Adapter<TestAnswerAdapter.ViewHolder>()  {


    class ViewHolder(private val binding: AdapterTestAnswerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TestQuestion) {
            for (i in 0 until item.ans.size) {
                val radioButton = RadioButton(binding.root.context)
                radioButton.text = item.ans[i]

                val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val margin = dpToPx(16)
                params.setMargins(0, 0, 0, margin)
                radioButton.layoutParams = params

                val padding = dpToPx(16)
                val paddingLeft = dpToPx(32)
                radioButton.setPadding(padding, padding, padding, padding)
                radioButton.compoundDrawablePadding = dpToPx(16)
                radioButton.setBackgroundResource(R.drawable.rounded_rect_bg_gray_border_25dp)

                radioButton.buttonDrawable = null
                radioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_radio_button, 0, 0, 0)

                radioButton.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                val font = binding.root.context.resources.getFont(R.font.outfit_regular)
                radioButton.typeface = font

                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                    ), intArrayOf(
                        ContextCompat.getColor(binding.root.context, R.color.red),
                        ContextCompat.getColor(binding.root.context, R.color.border_gray)
                    )
                )

//                radioButton.buttonTintList = colorStateList
                radioButton.id = View.generateViewId() // Optional: set a unique ID for the RadioButton
                binding.rgQuestions.addView(radioButton)
            }
        }

        private fun dpToPx(dp: Int): Int {
            val displayMetrics = binding.root.context.resources.displayMetrics
            return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterTestAnswerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
