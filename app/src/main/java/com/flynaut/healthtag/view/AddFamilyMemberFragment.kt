package com.flynaut.healthtag.view

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentAddFamilyBinding
import com.flynaut.healthtag.model.request.AddFamilyMemberRequest
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.viewmodel.FamilyMemberViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_add_family.*
import kotlinx.coroutines.handleCoroutineException

class AddFamilyMemberFragment : BaseFragment<FragmentAddFamilyBinding>() {

    // TODO: Rename and change types of parameters


    private lateinit var viewModel: FamilyMemberViewModel
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
        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[FamilyMemberViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_add_family
    }

    override fun initViews() {
        initObserver()
        binding.etPhone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(12))
        binding.etPhone.addTextChangedListener(object : TextWatcher {
            var length_before = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                length_before = s.length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (length_before < s.length) {
                    if (s.length == 3 || s.length == 7) s.append("-")
                    if (s.length > 3) {
                        if (Character.isDigit(s[3])) s.insert(3, "-")
                    }
                    if (s.length > 7) {
                        if (Character.isDigit(s[7])) s.insert(7, "-")
                    }
                }
                if (s.isNotEmpty() && s.length < 12) {
                    binding.etPhone.error = "Please enter valid phone number"
                } else {
                    binding.etPhone.error = null
                }
            }
        })

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

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (!Utils.isEmailValid(email)) {
                    binding.etEmail.error = "Invalid email address"
                } else {
                    binding.etEmail.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        var isFirstTimeRelationship = true
        val relationshipList = listOf(
            "Mother",
            "Father",
            "Brother",
            "Sister",
            "Grandmother",
            "Grandfather",
            "Aunt",
            "Uncle",
            "Cousin",
            "Niece",
            "Nephew",
            "Spouse",
            "Son",
            "Daughter",
            "Stepmother",
            "Stepfather",
            "Stepbrother",
            "Stepsister",
            "Stepson",
            "Stepdaughter",
            "In-laws",
            "Godmother",
            "Godfather",
            "Godson",
            "Goddaughter",
            "Friend",
            "Colleague",
            "Neighbor"
        )
        val relationshipAdapter = context?.let {  ArrayAdapter(it, R.layout.spinner_dropdown_item, relationshipList) }
        relationshipAdapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spRelationship.adapter = relationshipAdapter
        binding.etRelationship.setOnClickListener {
            binding.spRelationship.performClick()
        }
        var relation = ""
        binding.spRelationship.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (isFirstTimeRelationship) {
                    isFirstTimeRelationship = false
                    return
                }
                relation = parent.getItemAtPosition(position).toString()
                binding.etRelationship.text = relation
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.etRelationship.text = binding.spRelationship.selectedItem.toString()

            }
        }

        binding.btnSave.setOnClickListener {
            val isFormValid = areAllFieldsFilled(
                binding.etName,
                binding.etAddress,
                binding.etCity,
                binding.etEmail,
                binding.etPhone,
                binding.etState,
                binding.etZipcode
            )
            if(isFormValid){
                showProgressDialog()
                val familyMember = AddFamilyMemberRequest(
                    binding.etName.text.toString(),
                    binding.etAddress.text.toString(),
                    binding.etAddress2.text.toString(),
                    binding.etCity.text.toString(),
                    binding.etState.text.toString(),
                    binding.etZipcode.text.toString(),
                    binding.etPhone.text.toString().replace("-",""),
                    binding.etEmail.text.toString(),
                    binding.etRelationship.text.toString())


                viewModel.addFamilyMember(familyMember)
            }
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
        viewModel.addFamilyMemberApiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                activity?.onBackPressed()
            },500L)
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(completeProfileRequest: CompleteProfileRequest) =
            AddFamilyMemberFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable(ARG_COMPLETE_PROFILE_REQUEST, completeProfileRequest)

                }
            }
    }

}