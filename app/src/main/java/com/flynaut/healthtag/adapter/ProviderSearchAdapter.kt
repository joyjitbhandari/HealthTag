package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.databinding.AdapterProviderItemBinding
import com.flynaut.healthtag.model.response.ProviderDetails
import com.flynaut.healthtag.model.response.ProviderSearchDetails


class ProviderSearchAdapter(private val dataset: ArrayList<ProviderSearchDetails>) : RecyclerView.Adapter<ProviderSearchAdapter.ViewHolder>()  {


    class ViewHolder(private val binding: AdapterProviderItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProviderSearchDetails) {
            Glide.with(binding.root.context)
                .load("${item.image}")
                .into(binding.ivImg)

            binding.tvName.text=item.name
            binding.tvSpeciality.text=item.specialty
            binding.tvTenant.text=item.tenant
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterProviderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
