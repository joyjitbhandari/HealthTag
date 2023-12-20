package com.flynaut.healthtag.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flynaut.healthtag.model.response.CardDetails
import com.flynaut.healthtag.util.Utils.CARD_ID
import com.flynaut.healthtag.util.Utils.POSITION
import com.flynaut.healthtag.view.CardItemFragment

class CardPagerAdapter(fragmentActivity: FragmentActivity, private var items: List<CardDetails>, private val listener: CardItemFragment.CardCLickListener) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int {
            return items.count()
        }

        override fun createFragment(position: Int): Fragment {
            val data : CardDetails = items[position]
            val fragment = CardItemFragment(listener)
            val bundle = Bundle()
            bundle.putString(CARD_ID, data.id)
            bundle.putParcelable("KeyModel", data)
            bundle.putInt(POSITION,position)
            fragment.arguments = bundle
            return fragment
        }

    fun removeItem(position: Int) {
        if (position in items.indices) {
            items = items.toMutableList().apply {
                removeAt(position)
            }
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

}
