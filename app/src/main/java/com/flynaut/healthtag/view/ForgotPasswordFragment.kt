package com.flynaut.healthtag.view

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentForgotPasswordBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.CustomTypefaceSpan
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.viewmodel.ForgotPasswordViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory


class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    private lateinit var viewModel: ForgotPasswordViewModel

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
        )[ForgotPasswordViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_forgot_password
    }

    override fun initViews() {
        setCustomText()
        initObserver()
        binding.ivBack.setOnClickListener {
            activity?.finish()
        }

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

        binding.btnSubmit.setOnClickListener {
            if (binding.etEmail.text.isEmpty() || binding.etEmail.error != null) {
                binding.etEmail.error = "This field is required"
            } else{
//                val fields = HashMap<String, String>()
//                fields["email"] = binding.etEmail.text.toString()
//                showProgressDialog()
//                viewModel.forgotPassword(fields)
                (activity as MainActivity).replaceFragment(ResetPasswordFragment.newInstance(binding.etEmail.text.toString()),"ResetPasswordFragment",false)
            }

        }
    }

    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()
            (activity as MainActivity).replaceFragment(
                VerifyOTPFragment.forgotInstance(
                    true,
                    it.data.otp,
                    it.data.email
                ), "VerifyOTPFragment", false
            )
            Toast.makeText(requireContext(), "Otp sent successfully", Toast.LENGTH_SHORT).show()
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgotPasswordFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    fun setCustomText() {
        val context = binding.root.context
        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Forgot\n")
        val firstFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular) })
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            firstText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val secondText = SpannableString("your password?")
        val secondFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium) })
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.root.context.let {
            secondText.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        it,
                        R.color.black
                    )
                ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        builder.append(firstText)
        builder.append(secondText)

//        binding.tvResend.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTitle.text = builder
    }

}