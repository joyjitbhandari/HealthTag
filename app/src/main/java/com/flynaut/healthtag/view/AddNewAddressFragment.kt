package com.flynaut.healthtag.view

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentNewAddressBinding
import com.flynaut.healthtag.model.request.AddressRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.AddNewAddressViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

private const val ARG_IS_SIGNUP = "arg_is_signUp"
class AddNewAddressFragment : BaseFragment<FragmentNewAddressBinding>() {
    private lateinit var viewModel : AddNewAddressViewModel
    private lateinit var addressRequest: AddressRequest
    var addressType = ""
    private var isSignUpMode: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[AddNewAddressViewModel::class.java]
        arguments?.let {
            isSignUpMode = it.getBoolean(ARG_IS_SIGNUP)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_new_address
    }

    override fun initViews() {
        initObserver()
        binding.ivUser.setOnClickListener{
            (activity as MainActivity).replaceFragment(CompleteProfileFragment.newInstance(true), "CompleteProfileFragment")
        }

        var isFirstTime = true
        val addressTypes = listOf( "Select Address Type","Home", "Office", "Other")
        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, addressTypes) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spAddressType.adapter = adapter
        binding.etAddressType.setOnClickListener {
            binding.spAddressType.performClick()
        }

        binding.spAddressType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(isFirstTime){
                    isFirstTime = false
                    return
                }
                addressType = parent.getItemAtPosition(position).toString()
                binding.etAddressType.text = addressType
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.etAddressType.text = binding.spAddressType.selectedItem.toString()
            }
        }

        binding.etZipcode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isNotEmpty() && password.length < 5) {
                    binding.etZipcode.error = "Password length should be minimum of 8 characters."
                } else {
                    binding.etZipcode.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        var isFirstState = true
        val stateList = listOf( "Select State","Gujarat", "Punjab", "Bihar","Maharashtra")
        val stateAdapter = context?.let { ArrayAdapter(it, R.layout.spinner_dropdown_item, stateList) }
        adapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spState.adapter = stateAdapter
        binding.etState.setOnClickListener {
            binding.spState.performClick()
        }
        var state = ""
        binding.spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(isFirstState){
                    isFirstState = false
                    return
                }
                state = parent.getItemAtPosition(position).toString()
                binding.etState.text = state
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.etState.text = binding.spState.selectedItem.toString()

            }
        }

        binding.btnContinue.setOnClickListener {
            if(checkValidation()) {
                showProgressDialog()
                viewModel.addAddress(addressRequest)
            }
            else
                context?.showToast("Please fill the required details", Toast.LENGTH_SHORT)

        }
    }


    private fun checkValidation() : Boolean {
        val address = binding.etAddress.text.toString()
        val city = binding.etCity.text.toString()
        val state = binding.etState.text.toString()
        val zipcode = binding.etZipcode.text.toString()
        val phone = binding.etPhone.text.toString()

        if(addressType.isEmpty()){
            return false
        }
        if(addressType.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty() || phone.isEmpty() )


        addressRequest= AddressRequest(address,addressType,city,phone,state,zipcode)

        return true
    }



    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()
            context?.showToast(it.message, Toast.LENGTH_SHORT)
            Handler().postDelayed({
                activity?.onBackPressed()
            }, 500L)
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it,Toast.LENGTH_SHORT)
        })
    }
    companion object {
        @JvmStatic
        fun newInstance(isEditMode: Boolean) =
            CompleteProfileFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_SIGNUP, isEditMode)
                }
            }
    }

}