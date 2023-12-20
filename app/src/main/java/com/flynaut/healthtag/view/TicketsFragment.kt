package com.flynaut.healthtag.view

import android.view.Gravity
import android.widget.Toast
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.TicketsPagerAdapter
import com.flynaut.healthtag.databinding.FragmentTicketsBinding
import com.google.android.material.tabs.TabLayoutMediator


class TicketsFragment : BaseFragment<FragmentTicketsBinding>(),CreateTicketFragment.TicketCLickListener {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_tickets
    }

    override fun initViews() {
        binding.ivBack.setOnClickListener{
            (activity as MainActivity).onBackPressed()
        }
        setupViewPager()
        setupTabLayout()
        binding.ivAdd.setOnClickListener {
            val bottomSheetDialog = CreateTicketFragment(this)
            childFragmentManager.let { bottomSheetDialog.show(it, "CreateTicketFragment") }
          }
    }



    private fun setupTabLayout() {
        val tabText = arrayOf(getString(R.string.all), getString(R.string.open), getString(R.string.solved))

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabText[position]
            tab.view.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        }.attach()

//        for (i in 0 until binding.tabLayout.tabCount) {
//            val tab: TabLayout.Tab? = binding.tabLayout.getTabAt(i)
//            tab?.view?.gravity = Gravity.START or Gravity.CENTER_VERTICAL
//        }

    }

    private fun setupViewPager() {
        val adapter = activity?.let { TicketsPagerAdapter(it, 3) }
        binding.viewPager.adapter = adapter
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TicketsFragment().apply {

            }
    }

    override fun btnBackAfterSaveTicket() {
        Toast.makeText(requireContext(),"ssss",Toast.LENGTH_SHORT).show()
        setupViewPager()

    }

}