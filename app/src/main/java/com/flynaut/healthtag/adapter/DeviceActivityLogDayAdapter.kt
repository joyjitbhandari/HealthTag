package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.databinding.AdapterLogDayItemBinding
import com.flynaut.healthtag.model.response.Day

class DeviceActivityLogDayAdapter(private val logList: List<Day>) :
    RecyclerView.Adapter<DeviceActivityLogDayAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : AdapterLogDayItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(logItem: Day) {

            binding.tvLogDate.text = logItem.day
            val adapter = logItem.logs?.let { DeviceActivityLogListAdapter(it) }
            binding.rv.adapter = adapter
            adapter?.notifyDataSetChanged()
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding  = AdapterLogDayItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(logList[position])

    }

    override fun getItemCount(): Int {
        return logList.size
    }
}
