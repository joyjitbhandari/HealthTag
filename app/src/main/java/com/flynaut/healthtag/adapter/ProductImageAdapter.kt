package com.flynaut.healthtag.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.flynaut.healthtag.databinding.AdapterProductImageBinding
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL

class ProductImageAdapter(private val imageList: List<String>, private val onImageSelected: (String) -> ViewTarget<ImageView, Drawable>) : RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterProductImageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = imageList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onImageSelected.invoke(item)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = imageList.size

    class ViewHolder(private val binding: AdapterProductImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {

            Glide.with(binding.root.context)
                .load(IMAGE_URL+item)
                .into(binding.imageView)
        }
    }

}
