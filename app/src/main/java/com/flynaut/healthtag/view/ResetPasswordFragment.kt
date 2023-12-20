package com.flynaut.healthtag.view

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentResetPasswordBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.ResetPasswordViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

private const val ARG_EMAIL = "arg_email"

class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {

    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var email: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[ResetPasswordViewModel::class.java]
        arguments?.let {
            email = it.getString(ARG_EMAIL).toString()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_reset_password
    }

    override fun initViews() {
        initObserver()

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isNotEmpty() && password.length < 8) {
                    binding.etPassword.error = "Password length should be minimum of 8 characters."
                } else {
                    binding.etPassword.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = binding.etPassword.text.toString()
                val confirmPassword = s.toString()
                if (password.isNotEmpty() && password != confirmPassword) {
                    binding.etConfirmPassword.error = "Password do not match"
                } else {
                    binding.etConfirmPassword.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnSubmit.setOnClickListener {
            val isFormValid = areAllFieldsFilled(
                binding.etPassword,
                binding.etConfirmPassword
            )
            if (isFormValid) {
                showProgressDialog()
                val fields = HashMap<String, String>()
                fields["email"] = email
                fields["newPassword"] = binding.etPassword.text.toString()
                fields["confirmPassword"] = binding.etPassword.text.toString()
                viewModel.resetPassword(fields)
            }
        }

    }

    private fun areAllFieldsFilled(vararg fields: EditText): Boolean {
        var isValid = true
        for (field in fields) {
            if (field.text.toString().isEmpty()) {
                field.error = "This Field is required"
                isValid = false
            } else {
                if (field.error != null) {
                    isValid = false
                }
            }
        }
        return isValid
    }

    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                activity?.onBackPressed()
            }, 500L)
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(email: String) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMAIL, email)
                }
            }
    }

}