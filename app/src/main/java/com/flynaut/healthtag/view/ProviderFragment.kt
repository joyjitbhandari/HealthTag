package com.flynaut.healthtag.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.FaqCategoryDetailsAdapter
import com.flynaut.healthtag.adapter.ProviderAdapter
import com.flynaut.healthtag.adapter.ProviderSearchAdapter
import com.flynaut.healthtag.databinding.FragmentProvidersBinding
import com.flynaut.healthtag.model.request.SearchFaq
import com.flynaut.healthtag.model.response.*
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.BottomListSpacingItemDecoration
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.setVisible
import com.flynaut.healthtag.viewmodel.ProviderDetailsViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class ProviderFragment : BaseFragment<FragmentProvidersBinding>() {
    private lateinit var viewModel: ProviderDetailsViewModel
    private lateinit var providerSearchedList: ProviderSearchAdapter
    var providerArray = ArrayList<ProviderSearchDetails>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
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
        )[ProviderDetailsViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_providers
    }

    override fun initViews() {
        showProgressDialog()
        initObserver()
        viewModel.getAllProvider()

        binding.btnScanProvider.setOnClickListener {
            (activity as MainActivity).replaceFragment(ScannerFragment(), "ScannerFragment")
        }

        val layoutManager = LinearLayoutManager(context)
        providerSearchedList = ProviderSearchAdapter(providerArray)
        binding.rvProviderSearch.layoutManager = layoutManager
        binding.rvProviderSearch.adapter = providerSearchedList

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searched = s.toString()
                if (searched.isNotBlank()){
                    viewModel.searchProvider(searched)
                    binding.rvProviderSearch.visibility = View.VISIBLE
                    binding.rvProvider.visibility = View.GONE
                }
                else {
                    binding.rvProviderSearch.visibility = View.GONE
                    binding.rvProvider.visibility = View.VISIBLE
                    providerArray.clear()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun initObserver() {
        viewModel.providersResponse.observe(viewLifecycleOwner) { providersList ->
            hideProgressDialog()

            if (!providersList.isNullOrEmpty()){
                setListVisibility(true)
                val layoutManager = LinearLayoutManager(context)
                binding.rvProvider.layoutManager = layoutManager
                binding.rvProvider.setHasFixedSize(true)
                val adapter = ProviderAdapter(providersList)
                binding.rvProvider.adapter = adapter
                val spacing = resources.getDimensionPixelSize(R.dimen.dp_100)
                binding.rvProvider.addItemDecoration(BottomListSpacingItemDecoration(spacing))
            }
        }

        viewModel.searchProviderResponse.observe(viewLifecycleOwner) {

            providerArray.clear()
            if ((it.data?.size ?: 0) == 0){
                binding.rvProviderSearch.visibility = View.INVISIBLE
            }else{
                it.data.let { it?.let { it1 -> providerArray.addAll(it1) } }
                binding.rvProvider.visibility = View.GONE
            }
            providerSearchedList.notifyDataSetChanged()

        }
    }

    private fun setListVisibility(showProvider: Boolean) {
        binding.rvProvider.setVisible(showProvider)
        binding.clEmpty.setVisible(!showProvider)
    }
}