package com.flynaut.healthtag.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.DeviceItemReportBinding

class ReportDeviceAdapter (private val context: Context, private var deviceList: ArrayList<Drawable>
                           , private val listener: DeviceCLickListener) : RecyclerView.Adapter<ReportDeviceAdapter.MyViewHolder>() {
    private var selectedItemPosition: Int = 0
    interface DeviceCLickListener{
        fun onDeviceClicked(position: Int)
    }

    class MyViewHolder(var binding : DeviceItemReportBinding) : RecyclerView.ViewHolder(binding.root){


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DeviceItemReportBinding.inflate(LayoutInflater.from(context), parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imgDevice.setImageDrawable(deviceList[position])

        holder.binding.cardRoot.setCardBackgroundColor(
            if (position == selectedItemPosition)
                context.resources.getColor(R.color._E10044)
             else
                context.resources.getColor(R.color._EEEEEE))

        holder.itemView.setOnClickListener {
            listener.onDeviceClicked(position)
           setSelectedItem(position)
        }

    }

    private fun setSelectedItem(position: Int) {
        val previousSelectedItemPosition = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(previousSelectedItemPosition)
        notifyItemChanged(position)
    }
}