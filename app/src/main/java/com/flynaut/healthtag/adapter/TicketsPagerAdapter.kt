package com.flynaut.healthtag.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flynaut.healthtag.view.TicketListFragment

class TicketsPagerAdapter(fragmentActivity: FragmentActivity, private var totalCount: Int) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int {
            return totalCount
        }

        override fun createFragment(position: Int): Fragment {
            return TicketListFragment.newInstance(position)
        }
    }
