package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterDashboardBlogItemBinding
import com.flynaut.healthtag.model.CategoryItem
import com.flynaut.healthtag.model.response.BlogDetails
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.view.BlogDetailFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.TakeTestFragment


class DashboardBlogAdapter(val activity: FragmentActivity?, private val dataSet: List<BlogDetails>) : RecyclerView.Adapter<DashboardBlogAdapter.ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterDashboardBlogItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(activity, item)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = minOf(dataSet.size, 6)

    class ViewHolder(private val binding: AdapterDashboardBlogItemBinding) : RecyclerView.ViewHolder(binding.root) {
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
