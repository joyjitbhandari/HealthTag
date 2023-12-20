package com.flynaut.healthtag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.databinding.Device2ResultListItemReportBinding
import com.flynaut.healthtag.model.Device2ResultListItem

class Device2ResultListAdapter ( private var deviceList: ArrayList<Device2ResultListItem>
                                , private val listener: ReportCLickListener
) : RecyclerView.Adapter<Device2ResultListAdapter.MyViewHolder>() {

    interface ReportCLickListener{
        fun onDevice2ReportClicked(position: Int)
        fun onDevice2WriterNoteClicked(position: Int)
    }
    class MyViewHolder(var binding: Device2ResultListItemReportBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Device2ResultListItem) {

            binding.txtReportNo.text = item.id
            binding.txtHeartRateReport.text = item.heartRateReport
            binding.txtSpo2Report.text = item.spo2Report
            binding.txtDateTime.text = item.dateTime
            binding.txtDescription.text = item.note

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = Device2ResultListItemReportBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.imgDevice!!.setImageDrawable(deviceList.get(position))

        val item = deviceList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            listener.onDevice2ReportClicked(position)
        }

        holder.binding.txtDescription.setOnClickListener {
            listener.onDevice2WriterNoteClicked(position)
        }
    }

}