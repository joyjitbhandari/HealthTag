package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.FaqCategoryDetailsAdapter
import com.flynaut.healthtag.databinding.FragmentDevicesBinding
import com.flynaut.healthtag.model.response.DataItemFaq
import com.flynaut.healthtag.model.response.DataItemFaqDetail
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.FaqViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory


class FaqCategoryDetailsFragment : BaseFragment<FragmentDevicesBinding>() {
    private lateinit var adapterFqaDetailTicketList: FaqCategoryDetailsAdapter
    private lateinit var viewModel: FaqViewModel
    lateinit var categoryId:String
    private lateinit var titleName: String
    var ticketArray = ArrayList<DataItemFaqDetail>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[FaqViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_devices
    }

    override fun initViews() {
        showProgressDialog()
        setClick()
        initObserver()
        binding.tvTitle.text=titleName
        viewModel.getFaqCategoryDetail(categoryId)

        val layoutManager = LinearLayoutManager(context)
        adapterFqaDetailTicketList = FaqCategoryDetailsAdapter(ticketArray) { data ->
            (activity as MainActivity).replaceFragment(
                KnowMoreFragment.newInstance(data),
                "KnowMoreFragment"
            )
        }
        binding.rvQuestions.layoutManager = layoutManager
        binding.rvQuestions.adapter = adapterFqaDetailTicketList


    }

    private fun setClick() {
        binding.ivBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //callSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                callSearch(newText)
                //              }
                return true
            }

            fun callSearch(query: String?) {
                binding.progressSearch.visibility=View.VISIBLE
                if (query?.isEmpty() == true){
                    viewModel.getFaqCategoryDetail(categoryId)
                }else{
                    viewModel.getFaqSearch(query.toString())
                }

            }
        })
    }

    private fun initObserver() {
        viewModel.finalFaqDetailResponse.observe(viewLifecycleOwner) {
            ticketArray.clear()
            binding.progressSearch.visibility=View.GONE
            hideProgressDialog()
            if ((it.data?.size ?: 0) == 0) {
                binding.txtNoData.visibility = View.VISIBLE
                binding.rvQuestions.visibility = View.INVISIBLE
            } else {

                it.data.let { it?.let { it1 -> ticketArray.addAll(it1) } }
                binding.txtNoData.visibility = View.GONE
                binding.rvQuestions.visibility = View.VISIBLE
            }
            adapterFqaDetailTicketList.notifyDataSetChanged()
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            binding.progressSearch.visibility=View.GONE
            binding.txtNoData.visibility = View.VISIBLE
            binding.rvQuestions.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

    }

    companion object {
        @JvmStatic
        fun newInstance(model: DataItemFaq) =
            FaqCategoryDetailsFragment().apply {
                categoryId=model.id.toString()
                titleName=model.categoryName.toString()
            }
    }
}