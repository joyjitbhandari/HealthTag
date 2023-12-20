package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.OrderDetailAdapter
import com.flynaut.healthtag.databinding.FragmentOrderDetailBinding
import com.flynaut.healthtag.model.response.*
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.setVisible
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.MyOrderViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>(),
    CancelOrderFragment.OnButtonClick {

    private lateinit var adapter: OrderDetailAdapter
    lateinit var dataModel: DataItemOrderDetail
    private lateinit var viewModel: MyOrderViewModel
    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat("dd-MM-yyyy 'at' h:mm a", Locale.getDefault())
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
        )[MyOrderViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_order_detail
    }

    override fun initViews() {
//        showProgressDialog()
        initObserver()

//        dataModel.orderId?.let { viewModel.getSingleOrderDetails(it) }
        binding.tvTitle.text = "#${dataModel.orderId}"

        when (dataModel.status) {
            "Ordered" -> {
                binding.tvCancelOrder.setVisible(true)
                binding.tvOrderStatus.setVisible(false)

                binding.tvCancelOrder.setOnClickListener {
                    cancelOrder()
                }
            }
            "Shipped" -> {
                binding.tvCancelOrder.setVisible(true)
                binding.tvOrderStatus.setVisible(false)

                binding.tvCancelOrder.setOnClickListener {
                    cancelOrder()
                }
            }
            "Delivered" -> {
                binding.tvCancelOrder.setVisible(false)
                binding.tvOrderStatus.setVisible(true)
                binding.tvOrderStatus.text = dataModel.status
                val backgroundDrawable =
                    DrawableCompat.wrap(binding.tvOrderStatus.background).mutate()
                DrawableCompat.setTint(
                    backgroundDrawable,
                    ContextCompat.getColor(binding.root.context, R.color._4CA935)
                )
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            }
            "Canceled" -> {
                binding.tvCancelOrder.setVisible(false)
                binding.tvOrderStatus.setVisible(true)
                binding.tvOrderStatus.text = dataModel.status
                val backgroundDrawable =
                    DrawableCompat.wrap(binding.tvOrderStatus.background).mutate()
                DrawableCompat.setTint(
                    backgroundDrawable,
                    ContextCompat.getColor(binding.root.context, R.color.red)
                )
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            }
        }

        val layoutManager = LinearLayoutManager(context)
        adapter = dataModel.products?.let { OrderDetailAdapter(it) }!!
        binding.rvProducts.layoutManager = layoutManager
        binding.rvProducts.setHasFixedSize(true)
        binding.rvProducts.adapter = adapter

        binding.tvOrderNumber.text = "#${dataModel.orderId}"
        binding.tvTotal.text = "$${dataModel.totalAmount}"
        binding.tvPaymentMethod.text = dataModel.paymentMethod

        var productRate = 0
        for (i in 0 until dataModel.products!!.size) {
            dataModel.products!![i].productId.let { productId ->
                // Perform null check on the price of each product
                productId.price?.let {
                    productRate += it
                }
            }
        }
        binding.tvProductRate.text = "$$productRate"
        binding.tvDelivery.text = "$8"
        binding.tvTax.text="$0"

        if (!dataModel.orderDate.isNullOrEmpty()) {
            try {
                val date = inputDateFormat.parse(dataModel.orderDate)
                val formattedDate = outputDateFormat.format(date)
                binding.tvDate.text = formattedDate
            } catch (e: Exception) {
                e.printStackTrace()
                binding.tvDate.text = "Invalid Date"
            }
        }


        binding.tvPhoneNo.text = dataModel.addressId.phoneNumber

        binding.tvPhone.text="${dataModel.addressId.phoneNumber}"
        binding.tvDefaultAddress.text="Default Address : ${dataModel.addressId?.addressType}"
        binding.tvAddress.text="${dataModel.addressId?.address} ${dataModel.addressId?.city} ${dataModel.addressId?.state}  ${dataModel.addressId?.zipcode}"

    }


    private fun initObserver() {
        viewModel.cancelOrderResponse.observe(this, Observer {
            hideProgressDialog()
            val cancelOrderSuccessDialog = CancelOrderSuccessfulFragment()
            childFragmentManager.let {
                cancelOrderSuccessDialog.show(
                    it,
                    "CancelOrderSuccessfulFragment"
                )
            }
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it, Toast.LENGTH_SHORT)
        })
    }


    private fun cancelOrder() {
        val cancelOrderDialog = CancelOrderFragment(this)
        childFragmentManager.let {
            cancelOrderDialog.show(
                it,
                "CancelOrderFragment"
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(order: DataItemOrderDetail) =
            OrderDetailFragment().apply {
                dataModel = order
            }
    }

    override fun onYesClicked() {
        showProgressDialog()
        viewModel.cancelOrder(dataModel.orderId.toString())
    }
}