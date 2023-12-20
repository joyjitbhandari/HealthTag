package com.flynaut.healthtag.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentAddressBinding
import com.flynaut.healthtag.model.request.AddressRequest
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.*
import com.flynaut.healthtag.viewmodel.AddNewAddressViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.google.gson.Gson

private const val ARG_PHONE = "phone"

class AddressFragment : BaseFragment<FragmentAddressBinding>() {
    private lateinit var viewModel : AddNewAddressViewModel

//    // TODO: Rename and change types of parameters
//    private var completeProfileRequest: CompleteProfileRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            completeProfileRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                it.getParcelable(ARG_COMPLETE_PROFILE_REQUEST,CompleteProfileRequest::class.java )
//            } else {
//                it.getParcelable(ARG_COMPLETE_PROFILE_REQUEST )
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
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[AddNewAddressViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_address
    }

    override fun initViews() {
        setCustomText()
        initObserver()
        binding.etZipcode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isNotEmpty() && password.length < 5) {
                    binding.etZipcode.error = "Invalid Zipcode"
                } else {
                    binding.etZipcode.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

         binding.btnContinue.setOnClickListener {
             val phone = arguments?.getString(ARG_PHONE,"").toString()

             val isFormValid = areAllFieldsFilled(
                 binding.etAddress,
                 binding.etCity,
                 binding.etState,
                 binding.etZipcode,
             )
             if (isFormValid) {
                 showProgressDialog()
                 val address = binding.etAddress.text.toString()
                 val city = binding.etCity.text.toString()
                 val state = binding.etState.text.toString()
                 val zipcode = binding.etZipcode.text.toString()

                 viewModel.addAddress(AddressRequest(address, "Home", city, phone, state, zipcode))
             }

//             viewModel.completeProfile(SavedData.USER_ID,completeProfileRequest)
         }
    }


    private fun areAllFieldsFilled(vararg fields: EditText): Boolean {
        var isValid = true
        for (field in fields) {
            if (field.text.toString().isEmpty()) {
                field.error = "This Field is required"
                isValid = false
            }
            else{
                if(field.error != null){
                    isValid = false
                }
            }
        }
        return isValid
    }

    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()
            (activity as MainActivity).replaceFragment(DashboardFragment(), "DashboardFragment")
            context?.showToast(it.message, Toast.LENGTH_SHORT)
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it,Toast.LENGTH_SHORT)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(phoneNumber: String) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                putString(ARG_PHONE,phoneNumber)
                }
            }
    }

    private fun setCustomText() {
        val context = binding.root.context
        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Enter your\n")
        val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(ForegroundColorSpan(Color.BLACK), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString("complete address")
        val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium)})
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.root.context.let { secondText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.black) ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

        builder.append(firstText)
        builder.append(secondText)

//        binding.tvResend.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTitle.text = builder
    }

}