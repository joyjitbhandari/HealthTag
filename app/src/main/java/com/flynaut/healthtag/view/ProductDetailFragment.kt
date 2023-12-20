package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.ProductImageAdapter
import com.flynaut.healthtag.adapter.RecommendedProductAdapter
import com.flynaut.healthtag.databinding.FragmentProductDetailBinding
import com.flynaut.healthtag.model.request.AddRatingRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.setVisible
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.ProductDetailViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

private const val ARG_CATEGORY_ID = "arg_category_id"
private const val ARG_PRODUCT_ID = "arg_product_id"

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>(),
    RecommendedProductAdapter.ItemClickListener, SubmitReviewFragment.ClickListener {
    private var categoryId: String? = null
    private var productId: String? = null
    private lateinit var viewModel: ProductDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getString(ARG_CATEGORY_ID)
            productId = it.getString(ARG_PRODUCT_ID)
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
        )[ProductDetailViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_product_detail
    }

    override fun initViews() {
        initObserver()
        getProduct()
        viewModel.getRecommendedProducts(categoryId.toString(), productId.toString())

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvWriteReview.setOnClickListener {
            val bottomSheetDialog = SubmitReviewFragment(this)
            childFragmentManager?.let { bottomSheetDialog.show(it, "SubmitReviewFragment") }
        }

        binding.ivDescArrow.setOnClickListener {
            if (binding.tvDesc.isVisible) {
                binding.ivDescArrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                binding.tvDesc.setVisible(false)
            } else {
                binding.ivDescArrow.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                binding.tvDesc.setVisible(true)
            }
        }

        binding.ivHowToArrow.setOnClickListener {
            if (binding.tvHowTo.isVisible) {
                binding.ivHowToArrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                binding.tvHowTo.setVisible(false)
            } else {
                binding.ivHowToArrow.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                binding.tvHowTo.setVisible(true)
            }
        }

        binding.ivReviewArrow.setOnClickListener {
            if (binding.tvReview.isVisible) {
                binding.ivReviewArrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                binding.tvReview.setVisible(false)
            } else {
                binding.ivReviewArrow.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                binding.tvReview.setVisible(true)
            }
        }

    }

    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner) {
            val productDetail = it.data
            Glide.with(binding.root.context)
                .load(IMAGE_URL + productDetail.coverImage)
                .into(binding.ivProduct)
            val imageAdapter = ProductImageAdapter(it.data.images) { selectedImageResId ->
                Glide.with(binding.root.context)
                    .load(IMAGE_URL + selectedImageResId)
                    .into(binding.ivProduct)
            }

            binding.rvProducts.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvProducts.adapter = imageAdapter

            binding.tvProductName.text = it.data.productName
            binding.tvDeseaseName.text = it.data.category_name
            binding.tvProductDetail.text = it.data.shortDescription
            binding.tvDesc.text = it.data.longDescription
            binding.tvHowTo.text = it.data.howToUse
            binding.tvPrice.text = "$${it.data.price}"
            binding.ratingBar.rating = it.averageRating
            binding.tvReviewCount.text = "${it.averageRating.toInt()} (${it.allRating.size})"

            context?.showToast("Product fetched successfully", Toast.LENGTH_LONG)

        }

        viewModel.recommendedProducts.observe(viewLifecycleOwner) { recommendedProducts ->
            hideProgressDialog()
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val adapter = RecommendedProductAdapter(recommendedProducts, this)
            binding.rvRecommended.layoutManager = layoutManager
            binding.rvRecommended.setHasFixedSize(true)
            binding.rvRecommended.adapter = adapter
        }

        viewModel.addRatingResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            context?.showToast(it.message, Toast.LENGTH_SHORT)
            getProduct()
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it, Toast.LENGTH_SHORT)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryId: String, productId: String) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_ID, categoryId)
                    putString(ARG_PRODUCT_ID, productId)
                }
            }
    }

    override fun onRecommendedProductClick(productId: String) {
        (activity as MainActivity).replaceFragment(
            ProductDetailFragment.newInstance(
                categoryId.toString(),
                productId
            ), "ProductDetailFragment", false
        )
    }

    private fun getProduct(){
        showProgressDialog()
        viewModel.getProductDetail(productId.toString())
    }
    override fun onSubmitClick(rating: Int, review: String) {
        showProgressDialog()
        viewModel.addRating(productId.toString(), AddRatingRequest(rating, review))
    }
}