package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.databinding.AdapterAddressItemBinding
import com.flynaut.healthtag.model.response.Address
import com.flynaut.healthtag.util.PrefsManager
import com.google.gson.Gson

class AddressAdapter (private val dataSet: List<Address>) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterAddressItemBinding.inflate(inflater, parent, false)
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

    class ViewHolder(private val binding: AdapterAddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Address) {
            binding.tvAddressType.text = item.addressType
            binding.tvAddress.text = item.address
            binding.tvState.text = item.state
            binding.tvPhone.text = item.phoneNumber
            binding.rbDefault.isChecked = item.default

        }
    }

}
