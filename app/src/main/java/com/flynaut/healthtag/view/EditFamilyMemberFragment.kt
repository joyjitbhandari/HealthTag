package com.flynaut.healthtag.view

import android.os.Bundle
import android.os.Handler
import android.text.Editable
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
import com.flynaut.healthtag.databinding.FragmentEditFamilyBinding
import com.flynaut.healthtag.model.request.AddFamilyMemberRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.viewmodel.FamilyMemberViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class EditFamilyMemberFragment : BaseFragment<FragmentEditFamilyBinding>() {

    private var name: String? = null
    private var address: String? = null
    private var address2: String? = null
    private var city: String? = null
    private var state: String? = null
    private var zipcode: String? = null
    private var phone: String? = null
    private var email: String? = null
    private var relation: String? = null
    private lateinit var id: String

    private lateinit var viewModel: FamilyMemberViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            name = it.getString(ARG_NAME)
            address = it.getString(ARG_ADDRESS)
            address2 = it.getString(ARG_ADDRESS2)
            city = it.getString(ARG_CITY)
            state = it.getString(ARG_STATE)
            zipcode = it.getString(ARG_ZIP)
            phone = it.getString(ARG_PHONE)
            email = it.getString(ARG_EMAIL)
            relation = it.getString(ARG_RELATION)
            id = it.getString(ARG_ID).toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
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
        return R.layout.fragment_edit_family
    }

    override fun initViews() {
        initObserver()

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

        binding.etName.setText(name)
        binding.etAddress.setText(address)
        binding.etAddress2.setText(address2)
        binding.etCity.setText(city)
        binding.etState.setText(state)
        binding.etZipcode.setText(zipcode)
        binding.etPhone.setText(phone)
        binding.etEmail.setText(email)

        var isFirstTimeRelationship = false
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


        val mutableRelationList = relationshipList.toMutableList()
        val relationAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, mutableRelationList) }
        relationAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spRelationship.adapter = relationAdapter

        // Set the selected state value from the argument
        val relationPosition = relationAdapter?.getPosition(relation)
        if (relationPosition != null && relationPosition != -1) {
            binding.spRelationship.setSelection(relationPosition)
        } else {
            // State not found in the list, add it to the adapter and set it as selected
            relation?.let { mutableRelationList.add(it) }
            binding.spRelationship.setSelection(mutableRelationList.indexOf(relation))
        }

        var relation = ""
        binding.spRelationship.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long, ) {
                if (isFirstTimeRelationship) {
                    isFirstTimeRelationship = true
                    return
                }
                relation = parent.getItemAtPosition(position).toString()
                binding.etRelationship.text = relation
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.etRelationship.text = binding.spRelationship.selectedItem.toString()

            }
        }

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
            if (isFormValid) {
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
                viewModel.updateFamilyMember(id,familyMember)
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

    companion object {

        const val ARG_NAME = "arg_title"
        const val ARG_ADDRESS = "arg_address"
        const val ARG_ADDRESS2 =  "arg_address2"
        const val ARG_CITY = "arg_city"
        const val ARG_STATE = "arg_state"
        const val ARG_ZIP = "arg-zip"
        const val ARG_PHONE = "arg_phone"
        const val ARG_EMAIL = "arg_email"
        const val ARG_RELATION = "arg_relation"
        const val ARG_ID = "arg_id"

        @JvmStatic
        fun newInstance(name: String,address: String,address2: String,city: String,state: String,zipcode: String,phone: String,email: String,relation: String, id:String) =
            EditFamilyMemberFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_ADDRESS, address)
                    putString(ARG_ADDRESS2, address2)
                    putString(ARG_CITY, city)
                    putString(ARG_STATE, state)
                    putString(ARG_ZIP, zipcode)
                    putString(ARG_PHONE, phone)
                    putString(ARG_EMAIL, email)
                    putString(ARG_RELATION, relation)
                    putString(ARG_ID, id)
                }
            }
    }


    private fun initObserver() {
        viewModel.updateFamilyMemberApiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
//            Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                activity?.onBackPressed()
            },500L)
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }
}