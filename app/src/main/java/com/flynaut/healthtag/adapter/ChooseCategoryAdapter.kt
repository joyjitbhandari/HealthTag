package com.flynaut.healthtag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.flynaut.healthtag.databinding.AdapterChooseCategoryBinding
import com.flynaut.healthtag.model.response.Category
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.TakeTestFragment

class ChooseCategoryAdapter(
    private val context: Context,
    val activity: FragmentActivity?,
    private val items: List<Category>
) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            val binding = AdapterChooseCategoryBinding.inflate(inflater, parent, false)
            view = binding.root
            view.tag = binding
            binding.textView.text = items[position].name
//            binding.imageView.setImageResource(items[position].image)

//            binding.textView.text = items[position].name

            Glide.with(binding.root.context)
                .load(IMAGE_URL+items[position].image)
                .into(binding.imageView)

            binding.imageView.setOnClickListener{
                (activity as MainActivity).replaceFragment(TakeTestFragment.newInstance(items[position],items[position].name), "TakeTest")
            }
        }

        return view
    }
}
