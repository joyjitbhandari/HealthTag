package com.flynaut.healthtag.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentSignupBinding
import com.flynaut.healthtag.model.UserProfileDataSaved
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.network.TokenManager
import com.flynaut.healthtag.util.*
import com.flynaut.healthtag.util.PrefsManager.Companion.PREF_API_TOKEN
import com.flynaut.healthtag.util.Utils.isEmailValid
import com.flynaut.healthtag.viewmodel.SignupViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.google.gson.Gson

class SignupFragment : BaseFragment<FragmentSignupBinding>() {
    private lateinit var viewModel :SignupViewModel

    override fun getLayoutResId(): Int {
        return R.layout.fragment_signup
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[SignupViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initViews() {
        setCustomText()
        initObserver()

        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (!isEmailValid(email)) {
                    binding.etEmail.error = "Invalid email address"
                } else {
                    binding.etEmail.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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
                    binding. etConfirmPassword.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnSubmit.setOnClickListener {
            val isFormValid = areAllFieldsFilled(
                binding.etEmail,
                binding.etPassword,
                binding.etConfirmPassword
            )
            if (isFormValid) {
                showProgressDialog()
                viewModel.signup(binding.etEmail.text.toString(), binding.etPassword.text.toString())
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
        viewModel.signupResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            val prefsManager = PrefsManager.get()
            prefsManager.save(PREF_API_TOKEN,it.token)
            prefsManager.save(PrefsManager.PREF_USER_ID,it.data.userId.toString())
           // prefsManager.save(PrefsManager._ID,it.data._id)
            PrefsManager.get().save(
                PrefsManager.PREF_PROFILE,
                Gson().toJson(UserProfileDataSaved(it.data._id,"", "", it.data.email, "",""))
            )
            TokenManager.setToken(it.token)
            Log.d("token", "initObserver: ${it.token} ")
            (activity as MainActivity).replaceFragment(VerifyOTPFragment.signUpInstance(false, it.data.otp, it.data.email), "VerifyOTPFragment", false)
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it, Toast.LENGTH_SHORT)
        })
    }

    private fun setCustomText() {

        val builder = SpannableStringBuilder()
        val firstText = SpannableString("By clicking submit you agree to the\n")
        val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(ForegroundColorSpan(Color.BLACK), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString("Terms & Conditions")
        val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium)})
        val underlineSpan = UnderlineSpan()
        secondText.setSpan(underlineSpan, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val title = getString(R.string.terms_and_condition)
                (activity as MainActivity).replaceFragment(PrivacyPolicyFragment.newInstance(title,
                    Constant.WebViewKeys.TERMS_CONDITION_LINK ), "PrivacyPolicyFragment")
            }
            @Override
            override fun updateDrawState(ds : TextPaint) {
                context?.let { ds.color = ContextCompat.getColor(it, R.color.red) }
            }
        }
        secondText.setSpan(clickableSpan, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        val thirdText = SpannableString("of the app")
        val thirdFont =CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it,  R.font.outfit_regular)})
        thirdText.setSpan(thirdFont, 0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        thirdText.setSpan(ForegroundColorSpan(Color.BLACK), 0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder.append(firstText)
        builder.append(secondText)
        builder.append(" ")
        builder.append(thirdText)

        binding.tvAgreeTermsCondition.movementMethod = LinkMovementMethod.getInstance()
        binding.tvAgreeTermsCondition.text = builder
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}