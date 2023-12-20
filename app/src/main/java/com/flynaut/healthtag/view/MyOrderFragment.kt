package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.OrderAdapter
import com.flynaut.healthtag.databinding.FragmentMyOrderBinding
import com.flynaut.healthtag.model.response.DataItemOrderDetail
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.Constant
import com.flynaut.healthtag.util.Constant.Companion.monthsArray
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.MyOrderViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import java.util.*
import kotlin.collections.ArrayList

class MyOrderFragment : BaseFragment<FragmentMyOrderBinding>() {
    private lateinit var adapterOrder: OrderAdapter
    private lateinit var viewModel: MyOrderViewModel
    var month = "";
    var year = "";
    var orderType = "";
    var orderDetails = ArrayList<DataItemOrderDetail>()
    var isApiHit = false
    override fun getLayoutResId(): Int {
        return R.layout.fragment_my_order
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


    override fun initViews() {
        showProgressDialog()
        initObserver()
        // Call the function to load initial orders
//        loadInitialOrders()

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }


        val layoutManager = LinearLayoutManager(context)
        adapterOrder = OrderAdapter(orderDetails) { data ->
            (activity as MainActivity).replaceFragment(
                OrderDetailFragment.newInstance(data),
                "OrderDetailFragment"
            )
        }
        binding.rvOrder.layoutManager = layoutManager
        binding.rvOrder.setHasFixedSize(true)
        binding.rvOrder.adapter = adapterOrder

        val categoryList = listOf("All", "Placed", "Delivered", "Cancelled")
        val categoryAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, categoryList) }
        categoryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spCategory.adapter = categoryAdapter
        binding.tvSpCategory.setOnClickListener {
            binding.spCategory.performClick()

        }

        binding.spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val item = parent.getItemAtPosition(position)
                binding.tvSpCategory.text = item.toString()
                orderType = item.toString()
                if (orderType == "All"){
                    loadInitialOrders()
                    binding.spMonth.setSelection(0)
                    binding.spYear.setSelection(0)
                }
                else
                    if (isApiHit)
                        applyFilters()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        val monthAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, monthsArray()) }
        monthAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spMonth.adapter = monthAdapter

        binding.tvSpMonth.setOnClickListener {
            binding.spMonth.performClick()
        }

        binding.spMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val item = parent.getItemAtPosition(position) as Constant.Months
                if(item.monthInteger == "0"){
                    binding.tvSpMonth.text = item.name
                    month = ""
                    if (isApiHit)
                        applyFilters()
                }else{
                    binding.tvSpMonth.text = item.name
                    month = item.monthInteger
                    if (isApiHit)
                        applyFilters()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years  = (currentYear - 10..currentYear).map { it.toString() }

        val yearsWithSelectYear = mutableListOf<String>("Years")
        yearsWithSelectYear.addAll(years)

        val yearsAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, yearsWithSelectYear) }
        yearsAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spYear.adapter = yearsAdapter

        binding.tvSpYear.setOnClickListener {
            binding.spYear.performClick()
        }

        binding.spYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                if(item == "Years"){
                    binding.tvSpYear.text = item.toString()
                    year = ""
                    if (isApiHit)
                        applyFilters()
                }else{
                    binding.tvSpYear.text = item.toString()
                    year = item.toString()
                    if (isApiHit)
                        applyFilters()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initObserver() {
        viewModel.finalGetOrderResponse.observe(viewLifecycleOwner) { orderList ->
            hideProgressDialog()

            if ((orderList.data?.size ?: 0) == 0) {
                binding.txtNoData.visibility = View.VISIBLE
                binding.rvOrder.visibility = View.GONE
            } else {
                binding.txtNoData.visibility = View.GONE
                binding.rvOrder.visibility = View.VISIBLE
                orderDetails.clear()
                orderList.data?.let { orderDetails.addAll(it) }
                adapterOrder.notifyDataSetChanged()

            }
        }
        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            binding.txtNoData.visibility = View.VISIBLE
            binding.rvOrder.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyOrderFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun loadInitialOrders() {
        showProgressDialog()
        viewModel.getOrdersFilterDetails("", "", "ALL")
        isApiHit = true
    }

    private fun applyFilters() {
        if (isApiHit) {
            showProgressDialog()
            viewModel.getOrdersFilterDetails(month, year, orderType)
        }
    }


}