package com.flynaut.healthtag.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentOtpBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.CustomTypefaceSpan
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.VerifyOtpViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory


private const val ARG_FROM_SIGNUP = "from_signup"
private const val ARG_OTP = "arg_otp"
private const val ARG_EMAIL = "arg_email"
private const val ARG_FROM_FORGOT = "arg_from_forgot"
class VerifyOTPFragment : BaseFragment<FragmentOtpBinding>() {

    private lateinit var viewModel : VerifyOtpViewModel
    // TODO: Rename and change types of parameters
    private var fromSignup: Boolean = false
    private var fromForgot: Boolean = false
    private var otp: String? = null
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fromSignup = it.getBoolean(ARG_FROM_SIGNUP)
            fromForgot = it.getBoolean(ARG_FROM_FORGOT)
            otp = it.getString(ARG_OTP)
            email = it.getString(ARG_EMAIL)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[VerifyOtpViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_otp
    }

    override fun initViews() {
        initObserver()

        setTitleText()
        setCustomText()
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }

        val editTexts = arrayOf(binding.otpDigit1, binding.otpDigit2, binding.otpDigit3, binding.otpDigit4)

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i].setBackgroundResource(R.drawable.circle_blue_border)
                        editTexts[i + 1].requestFocus()
                    }
                    if (s?.length == 1 && i == editTexts.size - 1) {

                        // Create a handler to delay the start of the next activity
                        Handler().postDelayed({
                            editTexts[i].clearFocus()
                            // Start the next activity (replace MainActivity with your desired activity)
                            val fields = HashMap<String, String>()
                            fields["email"] = email ?: ""
                            fields["otp"] = otp ?: ""
                            showProgressDialog()
                            viewModel.verifyOtp(fields)
                        }, 1000L)
                    }
                }
            })
        }

    }

    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            if (fromForgot)
                (activity as MainActivity).replaceFragment(ResetPasswordFragment(), "ResetPasswordFragment",false)
            else
                (activity as MainActivity).replaceFragment(CompleteProfileFragment(), "CompleteProfileFragment",false)
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast( it, Toast.LENGTH_SHORT)

        })
    }

    private fun setTitleText() {

        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Enter the code\nsent to")
        val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(ForegroundColorSpan(Color.BLACK), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString("your email")
        val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium)})
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        context?.let { secondText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.black) ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

        builder.append(firstText)
        builder.append(" ")
        builder.append(secondText)

        binding.tvTitle.text = builder
    }

    private fun setCustomText() {

        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Didn’t receive the code?")
        val firstFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular)})
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        context?.let { firstText.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, R.color._444343)), 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

        val secondText = SpannableString("Resend")
        val secondFont = CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium)})
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        context?.let { secondText.setSpan(ForegroundColorSpan( ContextCompat.getColor(it, R.color.red) ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

//        val clickableSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//            }
//            @Override
//            override fun updateDrawState(ds : TextPaint) {
//                context?.let { ds.color = ContextCompat.getColor(it, R.color.red) }
//            }
//        }
//        secondText.setSpan(clickableSpan, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(firstText)
        builder.append(" ")
        builder.append(secondText)

//        binding.tvResend.movementMethod = LinkMovementMethod.getInstance()
        binding.tvResend.text = builder
    }

    companion object {
        @JvmStatic
        fun forgotInstance(fromForgot: Boolean, otp: String?, email: String) =
            VerifyOTPFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_FROM_FORGOT, fromForgot)
                    putString(ARG_OTP,otp)
                    putString(ARG_EMAIL,email)

                }
            }

        @JvmStatic
        fun signUpInstance(fromSignup: Boolean, otp: String?, email: String) =
            VerifyOTPFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_FROM_SIGNUP, fromSignup)
                    putString(ARG_OTP,otp)
                    putString(ARG_EMAIL,email)
                }
            }
    }


}