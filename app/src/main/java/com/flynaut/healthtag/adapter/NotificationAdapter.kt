package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterNotificationItemBinding
import com.flynaut.healthtag.model.ShopItem
import com.flynaut.healthtag.util.setVisible

class NotificationAdapter (private val dataSet: List<ShopItem>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterNotificationItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        val showBg = position == 0
        holder.bind(item, showBg)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: AdapterNotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShopItem, showBg: Boolean) {
            binding.ivNotification.setImageResource(item.imageResource)
            binding.tvTitle.text = item.title
            binding.tvTime.text = item.price
            binding.viewDivider.setVisible(!showBg)
            if(showBg) {
                binding.clRoot.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.red_09))
            } else {
                binding.clRoot.setBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.transparent))
            }
        }
    }

}
