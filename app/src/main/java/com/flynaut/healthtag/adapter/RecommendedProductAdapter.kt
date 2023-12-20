package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterRecommendeProductItemBinding
import com.flynaut.healthtag.model.CartItem
import com.flynaut.healthtag.model.response.RecommendedProductItems

class RecommendedProductAdapter (private val dataSet: List<RecommendedProductItems> , val listener: ItemClickListener) : RecyclerView.Adapter<RecommendedProductAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onRecommendedProductClick(productId: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterRecommendeProductItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.onRecommendedProductClick(item._id)
        }

    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(private val binding: AdapterRecommendeProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendedProductItems) {
            Glide.with(binding.root.context)
                .load("https://dev-healthtag-devapi.flynautstaging.com/uploads/${item.image}")
                .error(R.drawable.app_logo)
                .into(binding.ivProduct)
            binding.tvProductName.text = item.productName
            binding.tvPrice.text = "$${item.price}"
        }
    }

}
