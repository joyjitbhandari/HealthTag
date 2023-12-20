package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.NotificationAdapter
import com.flynaut.healthtag.databinding.FragmentNotificationBinding
import com.flynaut.healthtag.model.notificationItem

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivBack.setOnClickListener{
            (activity as MainActivity).onBackPressed()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_notification
    }

    override fun initViews() {
        val layoutManager = LinearLayoutManager(context)
        val adapter = NotificationAdapter(notificationItem)
        binding.rvNotifications.layoutManager = layoutManager
        binding.rvNotifications.setHasFixedSize(true)
        binding.rvNotifications.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {}
    }

}