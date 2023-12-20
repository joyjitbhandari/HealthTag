package com.flynaut.healthtag.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentReferFriendBinding
import com.flynaut.healthtag.model.request.CompleteProfileRequest
class ReferFriendFragment : BaseFragment<FragmentReferFriendBinding>() {
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
        return R.layout.fragment_refer_friend
    }

    override fun initViews() {
        val layoutManager = LinearLayoutManager(context)
//        val adapter = RewardAdapter(cartItems)
        binding.rvReward.layoutManager = layoutManager
        binding.rvReward.setHasFixedSize(true)
//        binding.rvReward.adapter = adapter

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
            ReferFriendFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable(ARG_COMPLETE_PROFILE_REQUEST, completeProfileRequest)

                }
            }
    }

}