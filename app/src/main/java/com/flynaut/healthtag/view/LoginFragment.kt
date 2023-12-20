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
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.databinding.FragmentLoginBinding
import com.flynaut.healthtag.model.UserProfileDataSaved
import com.flynaut.healthtag.network.TokenManager
import com.flynaut.healthtag.util.*
import com.flynaut.healthtag.util.PrefsManager.Companion.PREF_API_TOKEN
import com.flynaut.healthtag.util.PrefsManager.Companion.PREF_PROFILE
import com.flynaut.healthtag.util.PrefsManager.Companion.PREF_USER_ID
import com.flynaut.healthtag.viewmodel.LoginViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.google.gson.Gson

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var viewModel :LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[LoginViewModel::class.java]

        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

    override fun initViews() {
        setWelcomeText()
        setCustomText()
        initObserver()

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

        binding.btnLogin.setOnClickListener{
            val isFormValid = areAllFieldsFilled(
                binding.etEmail,
                binding.etPassword,
            )
            if (isFormValid) {
                showProgressDialog()
                viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            } else
                Toast.makeText(context, "Please fill the required details", Toast.LENGTH_SHORT).show()

    }
        binding.tvForgotPassword.setOnClickListener{
            (activity as MainActivity).replaceFragment(ForgotPasswordFragment(), "ForgotPasswordFragment",false)
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
        viewModel.loginResponse.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            val prefsManager = PrefsManager(requireContext())
            prefsManager.save(PREF_API_TOKEN,it.token)
            prefsManager.save(PREF_USER_ID,it.data.userId.toString())
           // prefsManager.save(_ID,it.data._id)
            PrefsManager.get().save(
                    PREF_PROFILE,
                    Gson().toJson(UserProfileDataSaved(it.data._id,it.data.firstName, it.data.lastName, it.data.email, it.data.userProfile.profilePicture,""))
                )
            TokenManager.setToken(it.token)
            Log.d("token", "initObserver: ${it.token}")
            Toast.makeText(requireContext(), "Logged In", Toast.LENGTH_SHORT).show()

            (activity as MainActivity).replaceFragment(DashboardFragment(),"DashboardFragment",false)
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast( it, Toast.LENGTH_SHORT)
        })
    }

    fun setWelcomeText() {

        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Good Morning\n")
        val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(ForegroundColorSpan(Color.BLACK), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString("Welcome")
        val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium)})
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.root.context.let { secondText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.black) ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

        val thirdText = SpannableString("to")
        val thirdFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
        thirdText.setSpan(thirdFont, 0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.root.context.let { thirdText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.black) ), 0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

        builder.append(firstText)
        builder.append(secondText)
        builder.append(" ")
        builder.append(thirdText)

        binding.tvWelcome.text = builder
    }

    private fun setCustomText() {

        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Don’t have an account?")
        val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(ForegroundColorSpan(Color.BLACK), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString("Signup")
        val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium)})
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                (activity as MainActivity).replaceFragment(SignupFragment(), "SignupFragment")
            }
            @Override
            override fun updateDrawState(ds : TextPaint) {
                context?.let { ds.color = ContextCompat.getColor(it, R.color.red) }
            }
        }
        secondText.setSpan(clickableSpan, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(firstText)
        builder.append(" ")
        builder.append(secondText)

        binding.tvSignup.movementMethod = LinkMovementMethod.getInstance()
        binding.tvSignup.text = builder
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}