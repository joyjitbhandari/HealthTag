package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.databinding.AdapterCartProductItemBinding
import com.flynaut.healthtag.model.response.CartListItem
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.viewmodel.CartViewModel

class CartAdapter (private val dataSet:  List<CartListItem>, private val cartViewModel: CartViewModel, private val listener: TotalPriceListener) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterCartProductItemBinding.inflate(inflater, parent, false)
        calculateTotalPrice()
        return ViewHolder(binding,cartViewModel)

    }

    interface TotalPriceListener {
        fun onTotalPriceUpdated(totalPrice: Double)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    private fun calculateTotalPrice() {
        var totalPrice = 0.0
        for (item in dataSet) {
            val quantity = item.quantity
            val price = item.productId.price
            val itemTotal = quantity * price
            totalPrice += itemTotal
        }

        listener.onTotalPriceUpdated(totalPrice)
    }
    fun updateQuantity(position: Int, newQuantity: Int) {
        val item = dataSet[position]
        item.quantity = newQuantity
        notifyItemChanged(position)
        calculateTotalPrice()
    }
    override fun getItemCount() = dataSet.size

    fun getItem(position: Int): CartListItem {
        return dataSet[position]
    }

    class ViewHolder(private val binding: AdapterCartProductItemBinding, private val cartViewModel: CartViewModel) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartListItem) {
            Glide.with(binding.root.context)
                .load(IMAGE_URL+item.productId.image)
                .into(binding.ivProduct)

            binding.tvProductName.text = item.productId.productName
            binding.tvPrice.text = "$ ${item.productId.price}"
            binding.tvQty.text = item.quantity.toString()

            binding.tvMinus.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    binding.tvQty.text = item.quantity.toString()
                }
            }
            binding.tvPlus.setOnClickListener {
                item.quantity++
                binding.tvQty.text = item.quantity.toString()
            }

        }
    }
}
