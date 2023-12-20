package com.flynaut.healthtag.view


import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentShopBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopFragment : BaseFragment<FragmentShopBinding>() {

    private val categoryIds: MutableList<String> = mutableListOf()
    var catId=""

    override fun getLayoutResId(): Int {
        return R.layout.fragment_shop
    }

    override fun initViews() {
        fetchTabData()
        binding.ivCart.setOnClickListener {
            (activity as MainActivity).replaceFragment(CartFragment(), "CartFragment")
        }

    }

    private fun fetchTabData(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.apiService.getCategory()
            if (response.isSuccessful){
                val categoryResponse = response.body()
                categoryResponse?.let {
                    val categories = it.data
                    val tabText = mutableListOf("All")
                    tabText.addAll(categories.map { category -> category.name })
                    categoryIds.clear()
                    categoryIds.add("")
                    categoryIds.addAll(categories.map { cat_id -> cat_id._id })
                    activity?.runOnUiThread {
                        setupTabLayout(tabText.toTypedArray())
                        val adapter = activity?.let { activity ->
                            ShopPagerAdapter(activity, tabText.size, categoryIds)
                        }
                        binding.viewPager.adapter = adapter
                        setupViewPager(tabText.size)
                    }
                }
            }
        }

    }

    private fun setupTabLayout(tabText: Array<String>) {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        val tabStrip = tabLayout.getChildAt(0) as? ViewGroup
        tabStrip?.let { strip ->
            val tabCount = strip.childCount
            val tabLayoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
            )
            for (i in 0 until tabCount) {
                val tabView = strip.getChildAt(i)
                tabView.layoutParams = tabLayoutParams

            }
        }
        viewPager.post {
            val adapter = ShopPagerAdapter(requireActivity(), tabText.size, categoryIds)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabText[position]
            }.attach()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                catId = categoryIds[tab.position]
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


    }

    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }



    private fun setupViewPager(categoryCount: Int) {
        val adapter = activity?.let { ShopPagerAdapter(it, categoryCount, categoryIds) }
        binding.viewPager.adapter = adapter
    }
    companion object {
        @JvmStatic
        fun newInstance(catId: String) =
            ShopFragment().apply {
                arguments = Bundle().apply {
                    putString("categoryId", catId)
                }
            }
    }
}

class ShopPagerAdapter(fragmentActivity: FragmentActivity, val totalCount: Int, private val categoryIds: List<String>) :
    FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return totalCount
        }
        override fun createFragment(position: Int): Fragment {
            val catId = categoryIds[position]
            return ShopListFragment.newInstance(catId)
        }
    }

