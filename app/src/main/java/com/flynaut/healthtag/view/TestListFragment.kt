package com.flynaut.healthtag.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentTestListBinding
import com.flynaut.healthtag.model.response.QuestionAnswer
import kotlin.math.roundToInt

class TestListFragment(private val question: QuestionAnswer, val position: Int, val count: Int, private val parentFragment: QuestionFragment) : BaseFragment<FragmentTestListBinding>() {

    var answerValue :Int? = null
    private var isRadioButtonSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_test_list
    }

    override fun initViews() {

        binding.tvQuesNo.text = "Question $position of $count"
        binding.tvQues.text = question.question
        for (i in 0 until question.answers.size) {
            val answer = question.answers[i]
            val radioButton = RadioButton(binding.root.context)
            radioButton.text = question.answers[i].answer

            val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val margin = dpToPx(16)
            params.setMargins(0, 0, 0, margin)
            radioButton.layoutParams = params

            val padding = dpToPx(18)
            val paddingLeft = dpToPx(32)
            radioButton.setPadding(padding, padding, padding, padding)
            radioButton.compoundDrawablePadding = dpToPx(18)
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
            radioButton.id = View.generateViewId()
            radioButton.tag = question.answers[i]// Optional: set a unique ID for the RadioButton
            radioButton.setOnCheckedChangeListener(null)
            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    answerValue = answer.value
                    question.selectedAnswerValue = answerValue
                    isRadioButtonSelected = true

                }
                parentFragment.setNextButtonEnabled(isRadioButtonSelected)

            }
            binding.rgQuestions.addView(radioButton)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = binding.root.context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

}