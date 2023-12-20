package com.flynaut.healthtag.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flynaut.healthtag.model.response.SubscriptionDetails
import com.flynaut.healthtag.view.SubscriptionItemFragment

class SubscriptionPagerAdapter(fragmentActivity: FragmentActivity, private var items: List<SubscriptionDetails>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun createFragment(position: Int): Fragment {
        val data : SubscriptionDetails = items[position]
        val fragment = SubscriptionItemFragment()
        val bundle = Bundle()
        bundle.putString("sub_id", data._id)
        fragment.arguments = bundle
        return fragment
    }
}
