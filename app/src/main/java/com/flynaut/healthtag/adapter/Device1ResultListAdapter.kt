package com.flynaut.healthtag.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.databinding.Device1ResultListItemReportBinding
import com.flynaut.healthtag.model.Device1ResultListItem

class Device1ResultListAdapter ( private var deviceList: ArrayList<Device1ResultListItem>
                                , private val listener: ReportCLickListener
) : RecyclerView.Adapter<Device1ResultListAdapter.MyViewHolder>() {

    interface ReportCLickListener{
        fun onDevice1ReportClicked(position: Int)
        fun onDevice1WriterNoteClicked(position: Int)
    }
    class MyViewHolder(var binding: Device1ResultListItemReportBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Device1ResultListItem) {

            binding.cardReportBg.setBackgroundResource(item.bgColor)
            binding.txtReportNo.text = item.id
            binding.txtReport.text = item.report
            binding.txtDescription.text = item.desc
            binding.txtDateTime.text = item.dateTime
            binding.txtReportTitle.text = item.heading
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = Device1ResultListItemReportBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.imgDevice!!.setImageDrawable(deviceList.get(position))

        holder.bind(deviceList[position])

        holder.itemView.setOnClickListener {
            listener.onDevice1ReportClicked(position)
        }

        holder.binding.txtDescription.setOnClickListener {
            listener.onDevice1WriterNoteClicked(position)
        }
    }


}