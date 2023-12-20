package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.databinding.AdapterShopItemBinding
import com.flynaut.healthtag.model.response.ShopProduct
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.view.CartFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.ProductDetailFragment


class ShopAdapter(val activity: FragmentActivity?, private val dataSet: List<ShopProduct>, private val addToCart: (ShopProduct) -> Unit) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterShopItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(activity, item , addToCart)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: AdapterShopItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: FragmentActivity?, item: ShopProduct, addToCart: (ShopProduct) -> Unit) {
            Glide.with(binding.root.context)
                .load(IMAGE_URL+item.coverImage)
                .into(binding.imageView)
            binding.tvPrice.text = "$${item.price}"
            binding.tvName.text = item.productName
            if (item.isAddedCart){
                binding.btnAddToCart.text = "Go to Cart"
                binding.btnAddToCart.setOnClickListener{
                    (activity as MainActivity).replaceFragment(CartFragment(), "CartFragment")
                }
            }else{
                binding.btnAddToCart.setOnClickListener { addToCart(item) }
            }
            binding.root.setOnClickListener{
                (activity as MainActivity).replaceFragment(ProductDetailFragment.newInstance(item.category, item._id), "ProductDetailFragment")
            }
        }
    }

}