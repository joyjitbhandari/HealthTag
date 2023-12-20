package com.flynaut.healthtag.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.CartAdapter
import com.flynaut.healthtag.adapter.RewardAdapter
import com.flynaut.healthtag.databinding.FragmentAddCardBinding
import com.flynaut.healthtag.databinding.FragmentAddressBinding
import com.flynaut.healthtag.databinding.FragmentReferFriendBinding
import com.flynaut.healthtag.databinding.FragmentRewardBinding
//import com.flynaut.healthtag.model.cartItems
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.CompleteProfileViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class RewardFragment : BaseFragment<FragmentRewardBinding>() {

    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        viewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(RetrofitClient.apiService)
//        )[CompleteProfileViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_reward
    }

    override fun initViews() {

    }


//    private fun initObserver() {
//        viewModel.apiResponse.observe(viewLifecycleOwner) {
//            hideProgressDialog()
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//        }
//
//        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
//            hideProgressDialog()
//            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//        })
//    }


    companion object {
        @JvmStatic
        fun newInstance(completeProfileRequest: CompleteProfileRequest) =
            RewardFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable(ARG_COMPLETE_PROFILE_REQUEST, completeProfileRequest)

                }
            }
    }

}