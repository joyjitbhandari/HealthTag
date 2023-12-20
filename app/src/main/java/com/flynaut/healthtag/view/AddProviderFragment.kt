package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentAddProviderBinding
import com.flynaut.healthtag.model.request.AddProviderRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.viewmodel.ProviderDetailsViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

private const val ARG_PROVIDER_ID = "arg_provider_id"
class AddProviderFragment : BaseFragment<FragmentAddProviderBinding>() {

    private var provider_id: String? = null
    private lateinit var viewModel: ProviderDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgressDialog()
        arguments?.let {
            provider_id = it.getString(ARG_PROVIDER_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[ProviderDetailsViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_add_provider
    }

    override fun initViews() {
        initObserver()
        provider_id?.let { viewModel.getProviderDetails(it) }

         binding.btnAddToList.setOnClickListener {
             provider_id?.let { it1 -> viewModel.setProviderDetails(it1) }
             Toast.makeText(requireContext(),"Provider added successfully",Toast.LENGTH_SHORT).show()
             (activity as MainActivity).replaceFragment(ProviderFragment(), "ProviderFragment")
         }

        binding.btnCancel.setOnClickListener{
            (activity as MainActivity).replaceFragment(ProviderFragment(), "ProviderFragment")
        }
    }

    private fun initObserver() {

        viewModel.singleProviderResponse.observe(viewLifecycleOwner){
            hideProgressDialog()
            binding.tvName.text = it.data.name
            binding.tvSpeciality.text = "Speciality: ${it.data.specialty}"
            binding.tvTenant.text = "Tenant: ${it.data.tenant}"
            Glide.with(binding.root.context).load(it.data.image).into(binding.ivProfile)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(providerId: String) =
            AddProviderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROVIDER_ID, providerId)
                }
            }
    }
}