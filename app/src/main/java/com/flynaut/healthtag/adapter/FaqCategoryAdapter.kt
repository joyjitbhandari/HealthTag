package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.databinding.FaqAdapterCategoryBinding
import com.flynaut.healthtag.model.response.DataItemFaq
import com.flynaut.healthtag.util.Constant

class FaqCategoryAdapter(private val dataSet: ArrayList<DataItemFaq>, private val onTicketClick: (DataItemFaq) -> Unit) : RecyclerView.Adapter<FaqCategoryAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FaqAdapterCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onTicketClick(item)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: FaqAdapterCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemFaq) {
            binding.tvTitleReports.text=item.categoryName
            binding.tvCountReport.text=item.faqsCount.toString()
//            Glide.with(binding.root.context)
//                .load("${Constant.IMAGE_URL}${item.image}")
//                .into(binding.ivImgReport)
        }

    }
}
