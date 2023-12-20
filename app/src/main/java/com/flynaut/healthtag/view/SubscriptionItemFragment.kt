package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentSubscriptionItemBinding
import com.flynaut.healthtag.model.response.SubscriptionDetails
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.SavedData.USER_ID
import com.flynaut.healthtag.viewmodel.SubscriptionViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class SubscriptionItemFragment : BaseFragment<FragmentSubscriptionItemBinding>() {

    private lateinit var viewModel: SubscriptionViewModel
    private lateinit var subId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId = it.getString("sub_id").toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[SubscriptionViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_subscription_item
    }

    override fun initViews() {
        showProgressDialog()
        initObserver()

        viewModel.getSubscriptionDetails(subId)


    }

    private fun initObserver(){
        viewModel.getSubscriptionApiResponse.observe(viewLifecycleOwner){
            hideProgressDialog()
            val data: SubscriptionDetails = it.data
            binding.tvPlanName.text = data.plan
            binding.tvPrice.text = "${data.price}"
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubscriptionItemFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}