package com.flynaut.healthtag.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.CartAdapter
import com.flynaut.healthtag.databinding.FragmentCartBinding
import com.flynaut.healthtag.model.response.CartItemResponse
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.*
import com.flynaut.healthtag.viewmodel.CartViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class CartFragment : BaseFragment<FragmentCartBinding>(), CartAdapter.TotalPriceListener {
    private lateinit var viewModel : CartViewModel
    private var price = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[CartViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_cart
    }

    override fun initViews() {
        initObserver()
        showProgressDialog()
        viewModel.getCart()
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }

        binding.tvClearBag.setOnClickListener{
            showProgressDialog()
            viewModel.clearCart()
        }

        binding.btnAddProducts.setOnClickListener {
            (activity as MainActivity).replaceFragment(DashboardFragment(),"DashboardFragment",false)
        }

        binding.btnCheckout.setOnClickListener {
            (activity as MainActivity).replaceFragment(CheckoutFragment(),"checkoutFragment")

        }

    }

    private fun initObserver() {
        viewModel.getCartResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            if(it.data.isNullOrEmpty()){
                setCartVisibility(true)
            }else{
                setCartVisibility(false)
                setCartData(it)
            }
        }

        viewModel.deleteCartResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            context?.showToast( it.message, Toast.LENGTH_SHORT)
            setCartVisibility(true)
        }

        viewModel.deleteCartProductResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            context?.showToast( it.message, Toast.LENGTH_SHORT)
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast( it, Toast.LENGTH_SHORT)
        })
    }

    private fun setCartVisibility(isEmpty: Boolean) {
        binding.clEmpty.setVisible(isEmpty)
        binding.clProduct.setVisible(!isEmpty)
        binding.btnCheckout.setVisible(!isEmpty)

        if(binding.clEmpty.visibility == View.VISIBLE)
            binding.tvClearBag.setTextColor(ContextCompat.getColor(binding.root.context,R.color.red_50))
        else
            binding.tvClearBag.setTextColor(ContextCompat.getColor(binding.root.context,R.color.red))
    }

    private fun setCartData(data: CartItemResponse) {
        val customLayoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val adapter = CartAdapter(data.data, viewModel, this)
        binding.rvProducts.layoutManager = customLayoutManager
        binding.rvProducts.setHasFixedSize(true)
        binding.rvProducts.adapter = adapter

        val swipeToDeleteProduct = SwipeToDeleteProduct(adapter, viewModel)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteProduct)
        itemTouchHelper.attachToRecyclerView(binding.rvProducts)
    }
    override fun onTotalPriceUpdated(totalPrice: Double) {
        binding.tvSubtotal.text = "$ $totalPrice"
        price = totalPrice.toInt()
    }

}