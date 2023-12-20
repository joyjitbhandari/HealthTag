package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterOrderItemBinding
import com.flynaut.healthtag.model.response.DataItemOrderDetail
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderAdapter(
    private val dataSet: ArrayList<DataItemOrderDetail>,
    private val onItemClicked: (DataItemOrderDetail) -> Unit,
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterOrderItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: AdapterOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        private val outputDateFormat = SimpleDateFormat("dd-MM-yyyy 'at' h:mm a", Locale.getDefault())
        fun bind(item: DataItemOrderDetail) {
            // Make sure to check if products is not null and not empty before accessing elements
            if (!item.products.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load("$IMAGE_URL${item.products[0].productId.image}")
                    .into(binding.ivProduct)

                binding.tvProductName.text = item.products[0].productId.productName
                binding.tvPrice.text = "$${item.totalAmount.toString()}"

                if (!item.orderDate.isNullOrEmpty()) {
                    try {
                        val date = inputDateFormat.parse(item.orderDate)
                        val formattedDate = outputDateFormat.format(date)
                        binding.tvTime.text = formattedDate
                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.tvTime.text = "Invalid Date"
                    }
                }

                item.products[0].productId.deliveryRate?.let { binding.ratingBar.setProgress(it) }
                binding.ratingBar.isClickable = false

                var totalItem = 0
                for (i in 0 until item.products.size){
                    totalItem += item.products[i].quantity
                }

                binding.tvTotalItem.text = totalItem.toString()
                binding.tvOrderStatus.text = item.status
                
                when (item.status) {
                    "Ordered" -> {
                        val backgroundDrawable =
                            DrawableCompat.wrap(binding.tvOrderStatus.background).mutate()
                        DrawableCompat.setTint(
                            backgroundDrawable,
                            ContextCompat.getColor(binding.root.context, R.color._F9F9F9)
                        )
                        binding.tvOrderStatus.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color._1D1C1A
                            )
                        )
                    }

                    "Shipped" -> {
                        val backgroundDrawable =
                            DrawableCompat.wrap(binding.tvOrderStatus.background).mutate()
                        DrawableCompat.setTint(
                            backgroundDrawable,
                            ContextCompat.getColor(binding.root.context, R.color._F9F9F9)
                        )
                        binding.tvOrderStatus.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color._1D1C1A
                            )
                        )
                    }

                    "Delivered" -> {
                        val backgroundDrawable =
                            DrawableCompat.wrap(binding.tvOrderStatus.background).mutate()
                        DrawableCompat.setTint(
                            backgroundDrawable,
                            ContextCompat.getColor(binding.root.context, R.color._4CA935)
                        )
                        binding.tvOrderStatus.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.white
                            )
                        )
                    }
                    "Canceled" -> {
                        val backgroundDrawable =
                            DrawableCompat.wrap(binding.tvOrderStatus.background).mutate()
                        DrawableCompat.setTint(
                            backgroundDrawable,
                            ContextCompat.getColor(binding.root.context, R.color.red)
                        )
                        binding.tvOrderStatus.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.white
                            )
                        )
                    }
                }
            }

        }
    }

}
