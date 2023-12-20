package com.flynaut.healthtag.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterTicketItemBinding
import com.flynaut.healthtag.model.response.DataItemAllTickets
import com.flynaut.healthtag.util.DateUtils
import com.flynaut.healthtag.util.setVisible

class TicketListAdapter(
    private val dataSet: ArrayList<DataItemAllTickets>,
    var isLimit: Boolean,
    private val onTicketClick: (DataItemAllTickets) -> Unit
) : RecyclerView.Adapter<TicketListAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterTicketItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onTicketClick(item)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() =
        if (isLimit) {
            if (dataSet.size > 3) {
                3
            } else {
                dataSet.size
            }
        } else {
            dataSet.size
        }


    class ViewHolder(private val binding: AdapterTicketItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemAllTickets) {
            //  binding.ivTicket.setImageResource(item.imageResource)
//            Glide.with(binding.root.context)
//                .load("${Constant.IMAGE_URL}${item.image}")
//                .into(binding.ivTicket)
            //  binding.ivTicket.setImageResource(item.imageResource)
            if (item.status == 1) {
                binding.tvStatus.text = "Solved"
            } else {
                binding.tvStatus.text = "Open"
            }
            binding.tvTicketTitle.text = item.ticketTitle
            binding.tvTime.text = DateUtils.getTimeAgo(item.updatedAt)
            binding.tvTicketDesc.text = item.description
            binding.tvTicketId.text = "Ticket ID : ${item.ticketId.toString()}"
            if (item.status == 1) {
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.green
                    )
                )
                binding.ivStatus.setVisible(true)
            } else {
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.yellow
                    )
                )
                binding.ivStatus.setVisible(false)
            }
        }

    }
}
