package com.flynaut.healthtag.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flynaut.healthtag.databinding.AdapterLogListItemBinding
import com.flynaut.healthtag.model.response.Logs
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.R

class DeviceActivityLogListAdapter(private val logList: List<Logs>) :
    RecyclerView.Adapter<DeviceActivityLogListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : AdapterLogListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(logs: Logs) {

            try{

                Glide.with(binding.root.context)
                    .load("https://dev-healthtag-devapi.flynautstaging.com/uploads/${logs.deviceImage}")
                    .placeholder(R.drawable.img_device1)
                    .error(R.drawable.img_device1)
                    .dontAnimate()
                    .into(binding.ivDeviceImg)

            }catch (e : Exception){
                Log.e("Ex",e.toString())
            }
            binding.tvDeviceName.text = logs.deviceName
            binding.tvConnectedTime.text = Utils.extractTime(logs.date.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding  = AdapterLogListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(logList[position])

    }

    override fun getItemCount(): Int {
        return logList.size
    }
}
