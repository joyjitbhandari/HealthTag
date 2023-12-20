package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentDashboardBinding
import com.flynaut.healthtag.model.UserProfileDataSaved
import com.flynaut.healthtag.model.request.CreateCustomerRequest
import com.flynaut.healthtag.model.response.UserProfile
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.network.TokenManager
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.util.SavedData
import com.flynaut.healthtag.util.SavedData.dashboardStatus
import com.flynaut.healthtag.viewmodel.CardViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.google.gson.Gson

class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {
    private lateinit var viewModel: CardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[CardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userProfile: UserProfileDataSaved = Gson().fromJson(
            PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
            UserProfileDataSaved::class.java)

        val profilePicture = userProfile?.profilePicture
        val imageUrl = if (profilePicture.isNullOrEmpty()) {
            R.drawable.profile_placeholder // Replace with the ID of your default profile picture drawable
        } else {
            IMAGE_URL + profilePicture
        }
       // val customerKey=  PrefsManager.get().getString(CREATE_CUSTOMER_KEY,"")
        if (userProfile._paymentId.isNullOrEmpty()){
            viewModel.createCustomer( CreateCustomerRequest(userProfile.firstName+" "+userProfile.lastName,userProfile.email))
        }
        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.ivProfile)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun initViews() {
        initObserver()
        // PrefsManager.initialize(requireContext())
//        viewModel.getProfile()
        SavedData.loadUserId()
        when (dashboardStatus) {
            "HomeFragment" -> addFragment(HomeFragment(), "HomeFragment")
            "ShopFragment" -> addFragment(ShopFragment(), "ShopFragment")
            "LogsFragment" -> addFragment(LogsFragment(), "LogsFragment")
            "ReportFragment" -> addFragment(ReportFragment(), "ReportFragment")
            else -> addFragment(HomeFragment(), "HomeFragment")
        }

        binding.ivProfile.setOnClickListener {
            (activity as MainActivity).replaceFragment(ProfileFragment(), "ProfileFragment")
        }
        binding.flNotification.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                NotificationFragment(),
                "NotificationFragment"
            )
        }

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    addFragment(HomeFragment(), "HomeFragment")
                    dashboardStatus = "HomeFragment"
                    true
                }
                R.id.navigation_shop -> {
                    addFragment(ShopFragment(), "ShopFragment")
                    dashboardStatus = "ShopFragment"
                    true
                }
                R.id.navigation_logs -> {
                    addFragment(LogsFragment(), "LogsFragment")
                    dashboardStatus = "LogsFragment"
                    true
                }
                R.id.navigation_reports -> {
                    addFragment(ReportFragment(), "ReportFragment")
                    dashboardStatus = "ReportFragment"
                    true
                }
                else -> false
            }
        }
    }

    private fun initObserver(){

        viewModel.createCustomerResponse.observe(this) {
           // PrefsManager(requireContext()).save(CREATE_CUSTOMER_KEY, it.data.id)
            val userProfile: UserProfileDataSaved = Gson().fromJson(
                PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
                UserProfileDataSaved::class.java)
            userProfile._paymentId= it.data.id

            PrefsManager.get().save(
                PrefsManager.PREF_PROFILE,
                Gson().toJson(UserProfileDataSaved(userProfile._id,userProfile.firstName,
                    userProfile.lastName, userProfile.email, userProfile.profilePicture, userProfile._paymentId))
            )
            SavedData.loadUserId()
        }
//        viewModel.toastMsg.observe(this,EventObserver{
//            context?.showToast(it, Toast.LENGTH_SHORT)
//        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}