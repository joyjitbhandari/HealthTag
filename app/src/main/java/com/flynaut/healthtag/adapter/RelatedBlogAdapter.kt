package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterBlogItemBinding
import com.flynaut.healthtag.databinding.AdapterRelatedBlogItemBinding
import com.flynaut.healthtag.model.CategoryItem
import com.flynaut.healthtag.model.response.BlogDetails
import com.flynaut.healthtag.util.Constant
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.view.BlogDetailFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.TakeTestFragment


class RelatedBlogAdapter(val activity: FragmentActivity?, private val dataSet: List<BlogDetails>) : RecyclerView.Adapter<RelatedBlogAdapter.ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterRelatedBlogItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(activity, item)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: AdapterRelatedBlogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: FragmentActivity?, item: BlogDetails) {
            Glide.with(binding.root.context)
                .load(IMAGE_URL+item.images[0])
                .into(binding.imageView)
            binding.textView.text = item.title
            binding.root.setOnClickListener{
                (activity as MainActivity).replaceFragment(BlogDetailFragment.newInstance(item._id,item.title,
                    IMAGE_URL+item.images[0],item.description),"BlogDetailFragment")
            }
        }
    }

}
