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
import com.flynaut.healthtag.adapter.AddressAdapter
import com.flynaut.healthtag.databinding.FragmentAddressBookBinding
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.BottomListSpacingItemDecoration
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.AddNewAddressViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class AddressBookFragment : BaseFragment<FragmentAddressBookBinding>() {
    private lateinit var viewModel : AddNewAddressViewModel

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
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[AddNewAddressViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_address_book
    }

    override fun initViews() {
        initObserver()
        showProgressDialog()
        viewModel.getAddress()

        binding.clAdd.setOnClickListener {
            (activity as MainActivity).replaceFragment(AddNewAddressFragment(), "AddNewAddressFragment",false)
        }

        binding.ivUser.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    private fun initObserver() {
        viewModel.getAddressResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()

            val layoutManager = LinearLayoutManager(context)
            val adapter = AddressAdapter(it.data)
            binding.rvAddress.layoutManager = layoutManager
            binding.rvAddress.setHasFixedSize(true)
            binding.rvAddress.adapter = adapter

            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
//            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(completeProfileRequest: CompleteProfileRequest) =
            AddressBookFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable(ARG_COMPLETE_PROFILE_REQUEST, completeProfileRequest)

                }
            }
    }

}