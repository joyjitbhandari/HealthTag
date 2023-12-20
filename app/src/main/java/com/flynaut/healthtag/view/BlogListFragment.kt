package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.BlogAdapter
import com.flynaut.healthtag.databinding.FragmentTicketListBinding
import com.flynaut.healthtag.model.blogItems
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.viewmodel.BlogsViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class BlogListFragment : BaseFragment<FragmentTicketListBinding>() {

    private lateinit var viewModel: BlogsViewModel
    private var categoryName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryName = arguments?.getString("categoryName", "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[BlogsViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_ticket_list
    }

    override fun initViews() {
        showProgressDialog()
        initObserver()
        if (categoryName=="")
            viewModel.getAllBlogs()
        else
            viewModel.getByCategory(categoryName)
    }

    private fun initObserver(){
        viewModel.blogsApiResponse.observe(viewLifecycleOwner){
            hideProgressDialog()
            val layoutManager = GridLayoutManager(context, 2)
            val adapter = BlogAdapter(activity, it.data)
            binding.rvTicket.layoutManager = layoutManager
            binding.rvTicket.setHasFixedSize(true)
            binding.rvTicket.adapter = adapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(cat_name: String) =
            BlogListFragment().apply {
                arguments = Bundle().apply {
                    putString("categoryName", cat_name)
                }
            }
    }

}