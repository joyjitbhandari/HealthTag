package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentMyDeciveBinding
import com.flynaut.healthtag.view.addDevice.AddDeviceFragment
import com.flynaut.healthtag.view.addDevice.DeviceListFragment
import kotlinx.android.synthetic.main.fragment_my_decive.*

class MyDeviceFragment : BaseFragment<FragmentMyDeciveBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_my_decive
    }

    override fun initViews() {
        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        // initial state
        button1.isSelected = true
        button1.setImageResource(R.drawable.ic_list_clicked)
        indicator.translationX = 0f

        //calling ist fragment
        addFragmentsToMyDeviceParentFragment(
            DeviceListFragment(),
            "DeviceListFragment",)

        button1.setOnClickListener {
            button1Select()
        }
        button2.setOnClickListener {
            button2Select()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MyDeviceFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun button1Select() {
        if (!button1.isSelected) {
            // state
            button1.isSelected = true
            button2.isSelected = false

            // position
            indicator.animate().translationX(0f).duration = 300

            // icon
            button1.setImageResource(R.drawable.ic_list_clicked)
            button2.setImageResource(R.drawable.ic_toggle_add)
        }
        addFragmentsToMyDeviceParentFragment(
            DeviceListFragment(),
            "DeviceListFragment",
        )
    }

    fun button2Select() {
        if (!button2.isSelected) {
            // state
            button1.isSelected = false
            button2.isSelected = true

            // indicator position
            indicator.animate().translationX(button2.x - button1.x).duration = 300

            // icon
            button1.setImageResource(R.drawable.ic_list)
            button2.setImageResource(R.drawable.ic_toggle_add_clicked)

        }
        addFragmentsToMyDeviceParentFragment(
            AddDeviceFragment(),
            "AddDeviceFragment",
        )
    }

    fun addFragmentsToMyDeviceParentFragment(
        fragment: Fragment,
        tag: String,
    ) {
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.myDeviceFragmentContainer, fragment, tag)
        transaction.commit()
    }

}