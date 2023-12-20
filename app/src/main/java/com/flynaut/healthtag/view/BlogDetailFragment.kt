package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.BlogAdapter
import com.flynaut.healthtag.adapter.RelatedBlogAdapter
import com.flynaut.healthtag.databinding.FragmentBlogDetailsBinding
import com.flynaut.healthtag.model.blogItems
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.viewmodel.BlogsViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class BlogDetailFragment : BaseFragment<FragmentBlogDetailsBinding>() {

    private lateinit var viewModel: BlogsViewModel

    private var title: String? = null
    private var image: String? = null
    private var description: String? = null
    private var blog_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            image = it.getString(ARG_IMAGE)
            description = it.getString(ARG_DESCRIPTION)
            blog_id = it.getString(ARG_BLOG_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[BlogsViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_blog_details
    }

    override fun initViews() {
        initObserver()
        blog_id?.let { viewModel.getRelatedBlogs(it) }
        binding.ivBack.setOnClickListener{
            (activity as MainActivity).replaceFragment(DashboardFragment(),"DashboardFragment")
        }

        title?.let { binding.tvTitle.text = it }
        image?.let { Glide.with(requireContext())
            .load(it)
            .into(binding.imageView) }
        description?.let { binding.tvTestDesc.text = it }
    }

    private fun initObserver(){
        viewModel.blogsApiResponse.observe(viewLifecycleOwner){
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = RelatedBlogAdapter(activity, it.data)
            binding.rvRelatedBlog.layoutManager = layoutManager
            binding.rvRelatedBlog.adapter = adapter
        }
    }

    companion object {

        const val ARG_TITLE = "arg_title"
        const val ARG_IMAGE = "arg_image"
        const val ARG_DESCRIPTION = "arg_desc"
        const val ARG_BLOG_ID = "arg_id"

        @JvmStatic
        fun newInstance(blogId: String,title: String, image: String, description: String) =
            BlogDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_IMAGE, image)
                    putString(ARG_DESCRIPTION, description)
                    putString(ARG_BLOG_ID, blogId)
                }
            }


    }

}