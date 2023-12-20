package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.databinding.AdapterDeviceItemBinding
import com.flynaut.healthtag.model.response.DataItemFaq
import com.flynaut.healthtag.model.response.DataItemFaqDetail

class FaqCategoryDetailsAdapter(private val dataSet: ArrayList<DataItemFaqDetail>, private val onTicketClick: (DataItemFaqDetail) -> Unit) : RecyclerView.Adapter<FaqCategoryDetailsAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterDeviceItemBinding.inflate(inflater, parent, false)
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

    class ViewHolder(private val binding: AdapterDeviceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemFaqDetail) {
         binding.tvQuestion.text=item.title
         binding.tvDesc.text=item.content
        }

    }
}
