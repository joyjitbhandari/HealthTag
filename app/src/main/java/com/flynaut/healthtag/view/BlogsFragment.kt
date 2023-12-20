package com.flynaut.healthtag.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentBlogsBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlogsFragment : BaseFragment<FragmentBlogsBinding>() {

    private val categoryNames: MutableList<String> = mutableListOf()
    override fun getLayoutResId(): Int {
        return R.layout.fragment_blogs
    }

    override fun initViews() {
        fetchTabData()
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
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
                    categoryNames.clear()
                    categoryNames.add("")
                    categoryNames.addAll(categories.map { cat_name -> cat_name.name })
                    activity?.runOnUiThread {
                        setupTabLayout(tabText.toTypedArray())
                        val adapter = activity?.let { activity ->
                            BlogsPagerAdapter(activity, tabText.size, categoryNames)
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

        viewPager.post {
            val adapter = BlogsPagerAdapter(requireActivity(), tabText.size, categoryNames)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabText[position]
            }.attach()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    private fun setupViewPager(categoryCount: Int) {
        val adapter = activity?.let { BlogsPagerAdapter(it, categoryCount, categoryNames) }
        binding.viewPager.adapter = adapter

    }

    companion object {
        @JvmStatic
        fun newInstance(cat_name: String) =
            BlogsFragment().apply {
                arguments = Bundle().apply {
                    putString("categoryName", cat_name)
                }
            }
    }
}

class BlogsPagerAdapter(fragmentActivity: FragmentActivity, private var totalCount: Int, private val categoryIds: List<String>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return totalCount
    }
    override fun createFragment(position: Int): Fragment {
        val catName = categoryIds[position]
        return BlogListFragment.newInstance(catName)
    }

}