package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.databinding.AdapterCartProductItemBinding
import com.flynaut.healthtag.databinding.AdapterNotificationItemBinding
import com.flynaut.healthtag.databinding.AdapterRewardItemBinding
import com.flynaut.healthtag.model.CartItem
import com.flynaut.healthtag.model.ShopItem

class RewardAdapter (private val dataSet: List<CartItem>) : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterRewardItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(item)

    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: AdapterRewardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {

        }
    }
}
