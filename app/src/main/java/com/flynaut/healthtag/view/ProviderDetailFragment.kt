package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentProviderDetailBinding
import com.flynaut.healthtag.model.response.DataItemProvider
import com.flynaut.healthtag.model.response.ProviderDetails
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.Constant
import com.flynaut.healthtag.viewmodel.ProviderDetailsViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class ProviderDetailFragment : BaseFragment<FragmentProviderDetailBinding>() {

    private lateinit var viewModel: ProviderDetailsViewModel
    private lateinit var providerDetail: DataItemProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            completeProfileRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                it.getParcelable(ARG_COMPLETE_PROFILE_REQUEST, CompleteProfileRequest::class.java )
//            } else {
//                it.getParcelable(ARG_COMPLETE_PROFILE_REQUEST)
//            }
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[ProviderDetailsViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_provider_detail
    }

    override fun initViews() {
         binding.btnAddToList.setOnClickListener {
//             (activity as MainActivity).replaceFragment(ProviderFragment.newInstance(true), "ProviderFragment")

         }
        setData()
    }

    private fun setData() {
        Glide.with(binding.root.context)
            .load("${Constant.IMAGE_URL}${providerDetail.image}")
            .into(binding.ivProfile)

        binding.tvName.text=providerDetail.name
        binding.tvSpeciality.text=providerDetail.specialty
        binding.tvTenant.text=providerDetail.tenant

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
        fun newInstance(detail: ProviderDetails) =
            ProviderDetailFragment().apply {
//               providerDetail= detail.name
            }
    }

}