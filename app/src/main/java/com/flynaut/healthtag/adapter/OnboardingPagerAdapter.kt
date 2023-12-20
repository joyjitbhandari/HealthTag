package com.flynaut.healthtag.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.OnboardingPageBinding
import com.flynaut.healthtag.model.OnboardingItem
import com.flynaut.healthtag.util.CustomTypefaceSpan

class OnboardingPagerAdapter(private val items: List<OnboardingItem>) : RecyclerView.Adapter<OnboardingPagerAdapter.ViewHolder>()  {


    class ViewHolder(private val binding: OnboardingPageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnboardingItem) {

            binding.imgOnboarding.setImageResource(item.image)
            setCustomText()
            binding.tvDescription.text = item.description
        }

        fun setCustomText() {
            val context = binding.root.context
            val builder = SpannableStringBuilder()

            val firstText = SpannableString("Lorem")
            val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
            firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            firstText.setSpan(ForegroundColorSpan(Color.BLACK), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val secondText = SpannableString("Ipsum is\nsimply dummy")
            val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium)})
            secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.root.context.let { secondText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.black) ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

            val thirdText = SpannableString("text")
            val thirdFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
            thirdText.setSpan(thirdFont, 0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.root.context.let { thirdText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.black) ), 0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

            builder.append(firstText)
            builder.append(" ")
            builder.append(secondText)
            builder.append(" ")
            builder.append(thirdText)

//        binding.tvResend.movementMethod = LinkMovementMethod.getInstance()
            binding.tvTitle.text = builder
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OnboardingPageBinding.inflate(inflater, parent, false)
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
