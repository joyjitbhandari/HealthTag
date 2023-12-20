package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.databinding.AdapterOrderDetailProductItemBinding
import com.flynaut.healthtag.databinding.Device1ResultListItemReportBinding
import com.flynaut.healthtag.model.response.Product
import com.flynaut.healthtag.util.Constant

class OrderDetailAdapter(private val dataSet: List<Product>/*, private val listener: OnClickReview*/) : RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
//    interface OnClickReview{
//        fun onReviewProductClick( position: Int)
//    }

    class ViewHolder(val binding: AdapterOrderDetailProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.tvProductName.text = item.productId.productName
            binding.tvQty.text = "Qty: ${item.quantity.toString()}"
            Glide.with(binding.root.context)
                .load("${Constant.IMAGE_URL}${item.productId.image}")
                .into(binding.ivProduct)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterOrderDetailProductItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(item)

//        holder.binding.tvRateOrder.setOnClickListener {
//            listener.onReviewProductClick(position)
//        }
    }
    override fun getItemCount() = dataSet.size
}
