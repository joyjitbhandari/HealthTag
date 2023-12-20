package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.SubscriptionPagerAdapter
import com.flynaut.healthtag.databinding.FragmentSubscriptionBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.SavedData
import com.flynaut.healthtag.util.SavedData.USER_ID
import com.flynaut.healthtag.util.setPreviewBothSideWithScale
import com.flynaut.healthtag.viewmodel.SubscriptionViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>() {

    private lateinit var viewModel : SubscriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[SubscriptionViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener{
            SavedData.dashboardStatus = ""
            (activity as MainActivity).onBackPressed()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_subscription
    }

    override fun initViews() {
        initObserver()

        viewModel.getAllSubscriptionDetails()
        binding.viewPager.setPreviewBothSideWithScale(15,20)

        binding.btnContinue.setOnClickListener {
            (activity as MainActivity).replaceFragment(DashboardFragment(), "DashboardFragment",false)
        }
    }

    private fun  initObserver(){
        viewModel.getAllSubscriptionList.observe(viewLifecycleOwner){ subscriptionList ->
            val subscriptionPagerAdapter = activity?.let { SubscriptionPagerAdapter(it,subscriptionList) }
            binding.viewPager.adapter = subscriptionPagerAdapter
            binding.indicator.setViewPager(binding.viewPager)
        }
    }

}