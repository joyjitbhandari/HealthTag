package com.flynaut.healthtag.view

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentAddCardBinding
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.model.request.CreateCardRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.SavedData.PREF_CREATE_CUSTOMER_KEY
import com.flynaut.healthtag.viewmodel.CardViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.CardParams
import com.stripe.android.model.Token
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AddCardFragment : BaseFragment<FragmentAddCardBinding>() {


    private lateinit var viewModel: CardViewModel
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

    private fun createStripeToken(card: CardParams) {
        val stripe = Stripe(requireContext(), "YOUR_STRIPE_PUBLISHABLE_KEY")
        stripe.createCardToken(
            card,
            callback = object : ApiResultCallback<Token> {
                override fun onError(e: Exception) {
                }

                override fun onSuccess(result: Token) {

                }
            }
        )
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
        )[CardViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_add_card
    }

    override fun initViews() {
        initObserver()

        binding.etCardNumber.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(19))
        binding.etCardNumber.addTextChangedListener(object : TextWatcher {
            var length_before = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                length_before = s.length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (length_before < s.length) {
                    if (s.length == 4 || s.length == 9 || s.length == 14) s.append("-")
                    if (s.length > 4) {
                        if (Character.isDigit(s[4])) s.insert(4, "-")
                    }
                    if (s.length > 9) {
                        if (Character.isDigit(s[9])) s.insert(9, "-")
                    }
                    if (s.length > 14) {
                        if (Character.isDigit(s[14])) s.insert(14, "-")
                    }
                }
                if (s.isNotEmpty() && s.length < 19) {
                    binding.etCardNumber.error = "Please enter valid card number"
                } else {
                    binding.etCardNumber.error = null
                }
            }
        })

        binding.etExpiry.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(5))
        binding.etExpiry.addTextChangedListener(object : TextWatcher {
            var length_before = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                length_before = s.length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
                val enteredDate = s.toString()

                if (length_before < s.length) {
                    if (s.length == 2) s.append("/")
                    if (s.length > 2) {
                        if (Character.isDigit(s[2])) s.insert(2, "/")
                    }
                }
                if (enteredDate.length == 5) {
                    val enteredMonth = enteredDate.substring(0, 2).toIntOrNull()
                    val enteredYear = enteredDate.substring(3, 5).toIntOrNull()
                    Log.d("date", "afterTextChanged: $enteredDate, $enteredMonth, $enteredYear")

                    if (enteredYear != null && enteredMonth != null && enteredYear >= currentYear && enteredMonth in 1..12) {
                        binding.etExpiry.error = null
                    } else {
                        binding.etExpiry.error = "Please enter a valid date"
                    }
                } else {
                    binding.etExpiry.error = "Please enter a valid date"
                }
            }
        })


        binding.etCvv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty() && s.length < 3) {
                    binding.etCvv.error = "Please enter valid cvv number"
                } else {
                    binding.etCvv.error = null
                }
            }
        })


        binding.btnSave.setOnClickListener {
            val isFormValid = areAllFieldsFilled(
                binding.etName,
                binding.etCardNumber,
                binding.etExpiry,
                binding.etCvv
            )
            if (isFormValid) {
                showProgressDialog()
//                val fields = HashMap<String, String>()
//                fields["name"] = binding.etName.text.toString()
//                fields["cardNumber"] = binding.etCardNumber.text.toString()
//                fields["expiryDate"] = binding.etExpiry.text.toString()
//                fields["cvv"] = binding.etCvv.text.toString()
                val splitArray = binding.etExpiry.text.toString().split("/")
                showProgressDialog()
//              val card=  Card.create()
                GlobalScope.launch {
                    createCardToken(
                        binding.etCardNumber.text.toString(),
                        splitArray[0].toInt(),
                        splitArray[1].toInt(),
                        binding.etCvv.text.toString(),
                        binding.etName.text.toString()
                    )
                }

//                createStripeToken(card)
//                viewModel.addCard(fields)

            }
        }
    }

    private suspend fun createCardToken(
        cardNumber: String,
        expMonth: Int,
        expYear: Int,
        cvc: String,
        name: String
    ): String {
        return suspendCoroutine { continuation: Continuation<String> ->
            // Create a Card object with the card details
           // val card = Card.create(cardNumber, expMonth, expYear, cvc)
            val card = CardParams(cardNumber, expMonth, expYear, cvc,name)

            // Check if the card is valid
            if (card != null ) {
                // Create a Stripe instance
                val stripe = Stripe(
                    requireContext(),
                    "pk_test_51MYswOEi9souWeY3qzPDphSyTMLudR0BMo0ZukA4H5hneIg31PNmQbz6zlABMgUBf3xWEWPVakaFEzu9lRgzWjYF00SAXoTbcz"
                )
                // Create a token for the card
                stripe.createCardToken(card, callback = object : ApiResultCallback<Token> {
                    override fun onSuccess(token: Token) {
                        continuation.resume(token.id)
                    //    val customerKey=  PrefsManager.get().getString(PrefsManager.CREATE_CUSTOMER_KEY,null)

                        viewModel.createCard(CreateCardRequest(PREF_CREATE_CUSTOMER_KEY,token.id))
                    }

                    override fun onError(error: Exception) {
                        hideProgressDialog()
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
               hideProgressDialog()
                Toast.makeText(requireContext(), "Invalid card details", Toast.LENGTH_SHORT).show()
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
//        viewModel.addCardApiResponse.observe(viewLifecycleOwner) {
//            hideProgressDialog()
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//            activity?.onBackPressed()
//        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            activity?.onBackPressed()
            Toast.makeText(requireContext(), "Card added successfully", Toast.LENGTH_SHORT).show()
        })

        viewModel.toastMsgError.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(completeProfileRequest: CompleteProfileRequest) =
            AddCardFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable(ARG_COMPLETE_PROFILE_REQUEST, completeProfileRequest)

                }
            }
    }

}