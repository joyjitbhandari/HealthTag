package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentResultBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.ResultViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

private const val ARG_CATEGORY_ID = "arg_category_id"
private const val ARG_TOTAL_RESULT = "arg_total_result"

class ResultFragment : BaseFragment<FragmentResultBinding>() {

    // TODO: Rename and change types of parameters
    private var categoryId: String? = null
    private var totalValue: Int? = null
    private lateinit var viewModel : ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getString(ARG_CATEGORY_ID)
            totalValue = it.getInt(ARG_TOTAL_RESULT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[ResultViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_result
    }

    override fun initViews() {
        initObserver()
        categoryId?.let {
//            val fields = HashMap<String, Int>()
//            fields["totalValues"] = totalValue ?: 0

//            showProgressDialog()
//            viewModel.getResult(it, fields)
        }

        binding.tvResult.text = totalValue.toString()

        binding.btnNext.setOnClickListener{
            (activity as MainActivity).replaceFragment(LoginFragment(), "LoginFragment")
        }

    }

    private fun initObserver() {
//        viewModel.apiResponse.observe(viewLifecycleOwner) { response ->
//            hideProgressDialog()
//            binding.tvResult.text = response.data.totalValues.toString()
//        }
//        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
//            hideProgressDialog()
//            context?.showToast(it)
//        })
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryId: String, totalValue: Int) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_ID, categoryId)
                    putInt(ARG_TOTAL_RESULT, totalValue)
                }
            }
    }


}