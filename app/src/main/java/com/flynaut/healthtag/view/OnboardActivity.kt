package com.flynaut.healthtag.view

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.OnboardingPagerAdapter
import com.flynaut.healthtag.databinding.ActivityOnboardingBinding
import com.flynaut.healthtag.model.OnboardingItem
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.util.PrefsManager.Companion.PREF_IS_ONBOARD
import me.relex.circleindicator.CircleIndicator3

const val ARG_IS_LOGIN = "isLogin"

class OnboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding) // Initialize binding
        PrefsManager.initialize(this)
        val onboardingItems = listOf(
            OnboardingItem(R.mipmap.onboard_image, getString(R.string.onboard_title), getString(R.string.onboard_desc)),
            OnboardingItem(R.mipmap.onboard_image1, getString(R.string.onboard_title), getString(R.string.onboard_desc)),
            OnboardingItem(R.mipmap.onboard_image2, getString(R.string.onboard_title), getString(R.string.onboard_desc))
        )

        val onboardingPagerAdapter = OnboardingPagerAdapter(onboardingItems)
        binding.viewPager.adapter = onboardingPagerAdapter
        binding.indicator.setViewPager(binding.viewPager)


        // Add a listener to the ViewPager2 to update the animation of the indicator
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.btnSkip.visibility = if (onboardingPagerAdapter.itemCount-1 == position) View.GONE else View.VISIBLE

                // Get the current indicator view and animate it
                val currentIndicator = binding.indicator.getChildAt(position)
                val animator = ObjectAnimator.ofFloat(currentIndicator, "rotation", 0f, 360f)
                animator.duration = 300
                animator.repeatCount = 1
                animator.interpolator = LinearInterpolator()
                animator.start()
                animator.setAutoCancel(true)
            }
        })
        binding.btnNext.setOnClickListener{
            val currentPage = binding.viewPager.currentItem
            val totalPage = onboardingPagerAdapter.itemCount
            if(currentPage == totalPage-1){
                startActivity(Intent(this@OnboardActivity, MainActivity::class.java))
            } else if (currentPage < totalPage) {
                    binding.viewPager.setCurrentItem(currentPage + 1, true)
                }
        }

        binding.btnSkip.setOnClickListener{
            val intent = Intent(this@OnboardActivity, MainActivity::class.java)
            intent.putExtra(ARG_IS_LOGIN, true)
            startActivity(intent)
        }
        val colors = intArrayOf(
            R.drawable.pager_indicator_green,
            R.drawable.pager_indicator_red,
            R.drawable.pager_indicator_yellow
        )

        val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until binding.indicator.childCount) {
                    val drawableRes = if (i == position) R.drawable.app_logo else colors[i]
                    binding.indicator.getChildAt(i).setBackgroundResource(drawableRes)
                }
            }
        }

        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }


}
