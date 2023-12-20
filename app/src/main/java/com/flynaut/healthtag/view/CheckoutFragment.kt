package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.CardPagerAdapter
import com.flynaut.healthtag.adapter.CartAdapter
import com.flynaut.healthtag.databinding.FragmentCheckoutBinding
import com.flynaut.healthtag.model.UserProfileDataSaved
import com.flynaut.healthtag.model.request.CreateChargeRequest
import com.flynaut.healthtag.model.request.GetCardRequest
import com.flynaut.healthtag.model.request.PlaceOrderRequest
import com.flynaut.healthtag.model.response.CardDetails
import com.flynaut.healthtag.model.response.CartItemResponse
import com.flynaut.healthtag.model.response.CartListItem
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.util.SavedData
import com.flynaut.healthtag.util.SavedData.PREF_ID
import com.flynaut.healthtag.util.SwipeToDeleteProduct
import com.flynaut.healthtag.util.setVisible
import com.flynaut.healthtag.viewmodel.AddNewAddressViewModel
import com.flynaut.healthtag.viewmodel.CartViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.google.gson.Gson

class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>(), CartAdapter.TotalPriceListener {

    private  var positionCard="0"
    private lateinit var addressId: String
    private var cardData = ArrayList<CardDetails>()
    private lateinit var viewModel: AddNewAddressViewModel
    private lateinit var cartViewModel: CartViewModel
    private var productPrice = 0
    private var deliveryPrice = 8


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[AddNewAddressViewModel::class.java]
        cartViewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[CartViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_checkout
    }

    override fun initViews() {
        initObserver()
        showProgressDialog()
        setClick()

        viewModel.getDefaultAddress()
        cartViewModel.getCart()


    }

    private fun setClick() {
        viewModel.getAllCardDetails(
            SavedData.PREF_ID, GetCardRequest(
                SavedData.PREF_CREATE_CUSTOMER_KEY
            )
        )
        binding.tvCardChange.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                PaymentFragment.newInstance(true),
                "PaymentFragment"
            )
        }
        binding.ivBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        binding.btnPay.setOnClickListener {

            if (cardData.size > 0) {
                showProgressDialog()
                val userProfile: UserProfileDataSaved = Gson().fromJson(
                    PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
                    UserProfileDataSaved::class.java
                )
                cartViewModel.createChargeApi(
                    PREF_ID, CreateChargeRequest(
                        userProfile.email, (productPrice + deliveryPrice).toString(),
                        "USD", cardData[0].id.toString(), "Demo Payment"
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Please add card", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnAddCard.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                PaymentFragment.newInstance(true),
                "PaymentFragment"
            )
        }
    }

    private fun initObserver() {
        viewModel.getAllCardList.observe(viewLifecycleOwner) { cardDetails ->
            if (cardDetails.isNotEmpty()) {
                cardData.clear()
                cardData.addAll(cardDetails)
                 positionCard = PrefsManager.get().getString(PrefsManager.KEY_CARD_SELECTION, "0")
                if (cardDetails.size>=positionCard.toInt()+1){
                    binding.tvName.text = cardDetails[positionCard.toInt()].name
                    binding.tvCard.text = "XXXX XXXX XXXX ${cardDetails[positionCard.toInt()].last4}"
                    binding.tvMonth.text =
                        cardDetails[positionCard.toInt()].expMonth.toString() + "/" + cardDetails[positionCard.toInt()].expYear.toString()
                }else{
                    binding.tvName.text = cardDetails[0].name
                    binding.tvCard.text = "XXXX XXXX XXXX ${cardDetails[0].last4}"
                    binding.tvMonth.text =
                        cardDetails[0].expMonth.toString() + "/" + cardDetails[0].expYear.toString()
                }
                binding.layoutAtm.visibility = View.VISIBLE
                binding.layoutNoCardAdded.visibility = View.INVISIBLE
            } else {
                binding.layoutAtm.visibility = View.INVISIBLE
                binding.layoutNoCardAdded.visibility = View.VISIBLE
            }

        }

        viewModel.toastMsgCard.observe(viewLifecycleOwner) {
            binding.layoutAtm.visibility = View.INVISIBLE
            binding.layoutNoCardAdded.visibility = View.VISIBLE
        }


        cartViewModel.toastMsgSuccess.observe(viewLifecycleOwner) {//afterPayment
            showProgressDialog()
            cartViewModel.placeOrderApi(PlaceOrderRequest(addressId, "online"))
        }

        cartViewModel.placeOrderSuccess.observe(viewLifecycleOwner) { //final payment response
            hideProgressDialog()
            Toast.makeText(requireContext(),"Order Placed successfully", Toast.LENGTH_SHORT).show()

            (activity as MainActivity).onBackPressed()
        }

        cartViewModel.toastMsg.observe(viewLifecycleOwner) { //card payment failure handle
            hideProgressDialog()
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.apiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            binding.tvDefaultAddress.text = "Default Address: ${it.data.addressType}"
            addressId = it.data._id
            binding.tvAddress.text =
                it.data.address + "," + it.data.addressLane2 + ", " + it.data.city
            binding.tvCountry.text = it.data.city
            binding.tvPhone.text = it.data.phoneNumber
        }

        cartViewModel.getCartResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            setCartData(it)
            binding.clCheckout.visibility = View.VISIBLE
        }
    }

    private fun setCartData(data: CartItemResponse) {
        val customLayoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val adapter = CartAdapter(data.data, cartViewModel, this)
        binding.rvProducts.layoutManager = customLayoutManager
        binding.rvProducts.setHasFixedSize(true)
        binding.rvProducts.adapter = adapter

    }

    override fun onTotalPriceUpdated(totalPrice: Double) {
        productPrice = totalPrice.toInt()
        binding.tvProductRate.text = "$ $productPrice"
        binding.tvDelivery.text = "$ $deliveryPrice"
        binding.tvTotal.text = "$ ${productPrice + deliveryPrice}"
        binding.btnPay.text = "Pay $ ${productPrice + deliveryPrice}"
    }

}