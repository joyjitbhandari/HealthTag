package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.OrderDetailAdapter
import com.flynaut.healthtag.databinding.FragmentReviewOrderBinding
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.model.response.Product

class ReviewOrderFragment : BaseFragment<FragmentReviewOrderBinding>(), CancelOrderFragment.OnButtonClick{

    private val arrayListCart= ArrayList<Product>()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        viewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(RetrofitClient.apiService)
//        )[CompleteProfileViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_review_order
    }

    override fun initViews() {
         binding.tvCancelOrder.setOnClickListener {
             val bottomSheetDialog = CancelOrderFragment(this)
             childFragmentManager?.let { bottomSheetDialog.show(it, "CancelOrderFragment") }
            }

//        val layoutManager = LinearLayoutManager(context)
//        val adapter = OrderDetailAdapter(arrayListCart)
//        binding.rvProducts.layoutManager = layoutManager
//        binding.rvProducts.setHasFixedSize(true)
//        binding.rvProducts.adapter = adapter

    }

    companion object {
        @JvmStatic
        fun newInstance(completeProfileRequest: CompleteProfileRequest) =
            ReviewOrderFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable(ARG_COMPLETE_PROFILE_REQUEST, completeProfileRequest)

                }
            }
    }

    override fun onYesClicked() {
        TODO("Not yet implemented")
    }


}