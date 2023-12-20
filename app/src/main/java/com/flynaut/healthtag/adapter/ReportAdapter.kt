package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.databinding.AdapterBlogItemBinding
import com.flynaut.healthtag.databinding.AdapterReportItemBinding
import com.flynaut.healthtag.model.CategoryItem
import com.flynaut.healthtag.model.DashboardItem
import com.flynaut.healthtag.view.BlogDetailFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.TakeTestFragment

class ReportAdapter(val activity: FragmentActivity?, private val dataSet: List<DashboardItem>) : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterReportItemBinding.inflate(inflater, parent, false)
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

    class ViewHolder(private val binding: AdapterReportItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: FragmentActivity?, item: DashboardItem) {
            binding.imageView.setImageResource(item.imageResource)
            binding.tvCount.text = item.count
            binding.tvMeasure.text = item.measurement
            binding.tvReportName.text = item.title
            binding.tvReportName.setTextColor(ContextCompat.getColor(binding.root.context, item.textColor))
            binding.clRoot.setBackgroundColor(ContextCompat.getColor(binding.root.context, item.bgColor))
            binding.root.setOnClickListener{
                (activity as MainActivity).replaceFragment(BlogDetailFragment(), "BlogDetailFragment")
            }
        }
    }

}
