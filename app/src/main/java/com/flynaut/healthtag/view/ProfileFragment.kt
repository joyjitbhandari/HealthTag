package com.flynaut.healthtag.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentProfileBinding
import com.flynaut.healthtag.model.UserProfileDataSaved
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.util.SavedData
import com.google.gson.Gson

class
ProfileFragment : BaseFragment<FragmentProfileBinding>() {

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
        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.ivProfile)
        binding.tvName.text = "${userProfile?.firstName} ${userProfile?.lastName}"
        binding.tvEmail.text = userProfile.email
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_profile
    }

    override fun initViews() {
        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.ivEdit.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                CompleteProfileFragment.newInstance(true),
                "CompleteProfileFragment"
            )
        }

        binding.llMyDevice.setOnClickListener {
            (activity as MainActivity).replaceFragment(MyDeviceFragment(), "MyDeviceFragment")
        }

        binding.llMyOrder.setOnClickListener {
            (activity as MainActivity).replaceFragment(MyOrderFragment(), "MyOrderFragment")
        }
        binding.llFamily.setOnClickListener {
            (activity as MainActivity).replaceFragment(FamilyFragment(), "FamilyFragment")
        }
        binding.llNotification.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                NotificationFragment(),
                "NotificationFragment"
            )
        }
        binding.llProvider.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                ProviderFragment(),
                "ProviderFragment"
            )
        }
        binding.llPayments.setOnClickListener {
            (activity as MainActivity).replaceFragment(PaymentFragment(), "PaymentFragment")
        }
        binding.llRefer.setOnClickListener {
            (activity as MainActivity).replaceFragment(ReferFriendFragment(), "ReferFriendFragment")
        }
        binding.llSupport.setOnClickListener {
            (activity as MainActivity).replaceFragment(FaqFragment(), "FaqFragment")
        }
        binding.llSubscription.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                SubscriptionFragment(),
                "SubscriptionFragment"
            )
        }
        binding.llLogOut.setOnClickListener{
            showLogOutDialog()
        }
        binding.tvFeedback.setOnClickListener {
            (activity as MainActivity).replaceFragment(FeedbackFragment(), "FeedbackFragment")
        }
        binding.tvAboutUs.setOnClickListener {
            (activity as MainActivity).replaceFragment(AboutUsFragment(), "AboutUsFragment")
        }

    }

    private fun showLogOutDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Log Out")
        alertDialogBuilder.setMessage("Are you sure you want to log out?")
        alertDialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            PrefsManager(requireContext()).clearPrefs()
            PrefsManager.get().save(PrefsManager.PREF_IS_ONBOARD, true)
            activity?.finishAffinity()

        }
        alertDialogBuilder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {}
    }

}