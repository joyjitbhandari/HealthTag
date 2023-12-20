package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentAboutBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.Constant.WebViewKeys.PRIVACY_POLICY_LINK
import com.flynaut.healthtag.util.Constant.WebViewKeys.TERMS_CONDITION_LINK
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.FaqViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class AboutUsFragment : BaseFragment<FragmentAboutBinding>() {

    private lateinit var viewModel: FaqViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[FaqViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_about
    }

    override fun initViews() {
        showProgressDialog()
        viewModel.getAboutUs()
        initObserver()
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }
        binding.tvTerms.setOnClickListener {
            val title = getString(R.string.terms_and_condition)
            (activity as MainActivity).replaceFragment(PrivacyPolicyFragment.newInstance(title,TERMS_CONDITION_LINK ), "PrivacyPolicyFragment")

        }
        binding.tvPrivacyPolicy.setOnClickListener {
            val title = getString(R.string.privacy_policy)
            (activity as MainActivity).replaceFragment(PrivacyPolicyFragment.newInstance(title,PRIVACY_POLICY_LINK ), "PrivacyPolicyFragment")
        }

    }
    private fun initObserver() {
        viewModel.finalAboutUsResponse.observe(viewLifecycleOwner) {
              hideProgressDialog()
            binding.tvTestDesc.text=it.data?.description
            //binding.tvTestDesc.text=it.data?.description
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutUsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}