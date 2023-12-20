package com.flynaut.healthtag.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.databinding.AdapterMyChatItemBinding
import com.flynaut.healthtag.databinding.AdapterSupportChatItemBinding
import com.flynaut.healthtag.model.ModelChat
import com.flynaut.healthtag.util.DateUtils.getTimeAgo

class ChatAdapter(
    private val items: ArrayList<ModelChat>,
  private val  imageUrl: Any,
    private val onItemClicked: (ModelChat) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class SupportChatViewHolder(private val binding: AdapterSupportChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ModelChat) {

            if (item.type == "image") {
                binding.tvTime.visibility = View.GONE
                binding.tvChat.visibility = View.GONE
                binding.ivProfile.visibility = View.GONE


                binding.tvTimeImage.visibility = View.VISIBLE
                binding.media.visibility = View.VISIBLE

                binding.tvTimeImage.text =getTimeAgo(item.timeStamp)

                Glide.with(binding.root.context)
                    .load(item.text)
                    .into(binding.media)
            } else {
                binding.tvTimeImage.visibility = View.GONE
                binding.media.visibility = View.GONE

                binding.tvTime.visibility = View.VISIBLE
                binding.tvChat.visibility = View.VISIBLE
                binding.ivProfile.visibility = View.VISIBLE

                binding.tvTime.text = getTimeAgo(item.timeStamp)
                binding.tvChat.text = item.text
            }
            binding.media.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    inner class MyChatViewHolder(private val binding: AdapterMyChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ModelChat) {
          //  val date = DateFormat.format("dd-MM-yyyy hh:mm a", item.timeStamp!!.toLong()).toString()
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.ivProfile)

            if (item.type == "image") {
                binding.tvTime.visibility = View.GONE
                binding.tvChat.visibility = View.GONE
                binding.ivProfile.visibility = View.GONE

                binding.tvTimeImage.visibility = View.VISIBLE
                binding.media.visibility = View.VISIBLE

                binding.tvTimeImage.text = item.timeStamp

                Glide.with(binding.root.context)
                    .load(item.text)
                    .into(binding.media)


            } else {
                binding.tvTimeImage.visibility = View.GONE
                binding.media.visibility = View.GONE

                binding.tvTime.visibility = View.VISIBLE
                binding.tvChat.visibility = View.VISIBLE
                binding.ivProfile.visibility = View.VISIBLE

                binding.tvTime.text = item.timeStamp
                binding.tvChat.text = item.text
            }
            binding.media.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].type != "Admin")
            TYPE_MY
        else
            TYPE_SUPPORT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SUPPORT) {
            val view = AdapterSupportChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SupportChatViewHolder(view)
        } else {
            val view =
                AdapterMyChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MyChatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (getItemViewType(position) == TYPE_SUPPORT) {
            (holder as SupportChatViewHolder).bind(item)
        } else {
            (holder as MyChatViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        private const val TYPE_SUPPORT = 0
        private const val TYPE_MY = 1
    }

}
