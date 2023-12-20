package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.ShopAdapter
import com.flynaut.healthtag.databinding.FragmentTicketListBinding
import com.flynaut.healthtag.model.request.AddToCartRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.*
import com.flynaut.healthtag.util.SavedData.PREF_ID
import com.flynaut.healthtag.viewmodel.ShopViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class ShopListFragment : BaseFragment<FragmentTicketListBinding>() {
    private lateinit var viewModel : ShopViewModel
    private var categoryId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getString("categoryId", "") ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[ShopViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_ticket_list
    }

    override fun initViews() {
        initObserver()
        showProgressDialog()
        if (categoryId=="")
            viewModel.getShopProduct()
        else
            viewModel.getProductByCategory(categoryId)

    }

    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            val layoutManager = GridLayoutManager(context, 2)
            val adapter = ShopAdapter(activity, it.data) { product ->
                showProgressDialog()
                viewModel.addToCart(AddToCartRequest(PREF_ID, product._id, 1))
//                Toast.makeText(requireContext(),"Item added to cart",Toast.LENGTH_SHORT).show()


            }
            binding.rvTicket.layoutManager = layoutManager
            binding.rvTicket.setHasFixedSize(true)
            binding.rvTicket.adapter = adapter
            val spacing = resources.getDimensionPixelSize(R.dimen.dp_16)
            val bottomSpacing = resources.getDimensionPixelSize(R.dimen.dp_70)
            binding.rvTicket.addItemDecoration(ItemSpacingShopList(spacing,bottomSpacing))
        }

        viewModel.catResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()

            val layoutManager = GridLayoutManager(context, 2)
            val adapter = ShopAdapter(activity, it.data) { product ->
                showProgressDialog()
                viewModel.addToCart(AddToCartRequest(PREF_ID, product._id, 1))
                Toast.makeText(requireContext(),"Item added to cart",Toast.LENGTH_SHORT).show()
            }
            binding.rvTicket.layoutManager = layoutManager
            binding.rvTicket.setHasFixedSize(true)
            binding.rvTicket.adapter = adapter
            val spacing = resources.getDimensionPixelSize(R.dimen.dp_16)
            val bottomSpacing = resources.getDimensionPixelSize(R.dimen.dp_70)
            binding.rvTicket.addItemDecoration(ItemSpacingShopList(spacing,bottomSpacing))
        }

        viewModel.cartResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()

            val addedToCartDialog = AddedToCartDialogFragment()
            childFragmentManager?.let {
                addedToCartDialog.show(it, "AddedToCardDialogFragment")
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(categoryId: String) =
            ShopListFragment().apply {
                arguments = Bundle().apply {
                    putString("categoryId", categoryId)
                }
            }
    }
}