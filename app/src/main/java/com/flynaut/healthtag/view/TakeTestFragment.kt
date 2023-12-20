package com.flynaut.healthtag.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.BundleCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.ChooseCategoryAdapter
import com.flynaut.healthtag.databinding.FragmentTakeTestBinding
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.model.response.Category
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.Utils.getParcelable
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.CategoryViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

private const val ARG_CATEGORY = "arg_category"
private const val ARG_HEADING = "arg_heading"

class TakeTestFragment : BaseFragment<FragmentTakeTestBinding>() {

    // TODO: Rename and change types of parameters
    private var category: Category? = null
    private var heading: String? = null
    private lateinit var viewModel : CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_CATEGORY,Category::class.java )
            } else {
                it.getParcelable(ARG_CATEGORY)
            }
            heading = it.getString(ARG_HEADING)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[CategoryViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = heading
        binding.ivBack.setOnClickListener{
            (activity as MainActivity).onBackPressed()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_take_test
    }

    override fun initViews() {
        initObserver()
        category?._id?.let {
            showProgressDialog()
            viewModel.getCategoryDetail(it)
        }

        Glide.with(binding.root.context)
            .load("https://dev-healthtag-devapi.flynautstaging.com/uploads/${category?.image}")
            .into(binding.ivTest)


        val items = listOf( "Item 1", "Item 2", "Item 3")
        val adapter = context?.let { ArrayAdapter(it, R.layout.spinner_dropdown_item, items) }
        adapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spTest.adapter = adapter

        binding.tvSpTest.setOnClickListener {
            binding.spTest.performClick()
        }
        binding.spTest.setSelection(1,false)

        binding.spTest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position)
                binding.tvSpTest.text = item.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.switchOther.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tvSpTest.visibility =View.VISIBLE
                binding.spTest.visibility =View.INVISIBLE
            } else {
                binding.tvSpTest.visibility =View.GONE
                binding.spTest.visibility =View.GONE            }
        }

        binding.btnBegin.setOnClickListener{
            category?._id?.let {
                (activity as MainActivity).replaceFragment(QuestionFragment.newInstance(it), "QuestionFragment")
            }
        }

    }

    private fun initObserver() {
        viewModel.categoryDetailResponse.observe(viewLifecycleOwner) { response ->
            hideProgressDialog()
            binding.tvTestDesc.text = response.data.details
            binding.tvTitle.text = "${response.data.name} Control Test"
        }
        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it)

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(category: Category,heading:String) =
            TakeTestFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CATEGORY, category)
                    putString(ARG_HEADING, heading)
                }
            }
    }


}