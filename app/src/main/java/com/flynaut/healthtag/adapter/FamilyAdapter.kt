package com.flynaut.healthtag.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterFamilyItemBinding
import com.flynaut.healthtag.model.response.FamilyMember
import com.flynaut.healthtag.view.EditFamilyMemberFragment
import com.flynaut.healthtag.viewmodel.FamilyMemberViewModel

class FamilyAdapter(val activity: FragmentActivity?, private val dataset: List<FamilyMember>, private val viewModel: FamilyMemberViewModel) :
    RecyclerView.Adapter<FamilyAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterFamilyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: FragmentActivity?,item: FamilyMember) {
            binding.tvAddress.text = item.address
            binding.tvEmail.text = item.email
            binding.tvName.text = item.name
            binding.tvPhone.text = item.phoneNo.toString()
            binding.tvRelation.text = item.relation

            binding.root.setOnClickListener{
                val fragment = EditFamilyMemberFragment.newInstance(
                    item.name,item.address,item.addressLine2,item.city,item.state,item.zipcode,item.phoneNo.toString(),item.email,item.relation,item._id
                )
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterFamilyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return dataset.size
    }
    fun getItem(position: Int): FamilyMember {
        return dataset[position]
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activity,dataset[position])


//        holder.itemView.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_UP) {
//                Toast.makeText(holder.itemView.context,"clicked on touch: "+dataset[position]._id,Toast.LENGTH_SHORT).show()
//                val swipeX = event.x
//                val itemViewWidth = holder.itemView.width
//                if (swipeX < itemViewWidth * 0.75) {
//
//                    return@setOnTouchListener true
//                }
//            }
//            return@setOnTouchListener false
//        }

    }
}



