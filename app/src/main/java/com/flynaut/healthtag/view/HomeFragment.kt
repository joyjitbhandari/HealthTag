package com.flynaut.healthtag.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.DashboardBlogAdapter
import com.flynaut.healthtag.adapter.ReportAdapter
import com.flynaut.healthtag.databinding.FragmentHomeBinding
import com.flynaut.healthtag.model.UserProfileDataSaved
import com.flynaut.healthtag.model.dashboardItems
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.CustomTypefaceSpan
import com.flynaut.healthtag.util.ItemSpacingHomeCards
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.viewmodel.BlogsViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.google.gson.Gson


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var viewModel: BlogsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[BlogsViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initViews() {
        initObserver()
        setCustomText()
        viewModel.getAllBlogs()
        binding.tvViewAll.setOnClickListener {
            (activity as MainActivity).replaceFragment(BlogsFragment(), "BlogsFragment")
        }

        val userProfile = Gson().fromJson(
            PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
            UserProfileDataSaved::class.java
        )

        val userList = listOf("${userProfile?.firstName} ${userProfile?.lastName}", "Member1", "Member2")

        val userAdapter =
            context?.let { ArrayAdapter(it, R.layout.spinner_dropdown_item, userList) }
        userAdapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spUser.adapter = userAdapter
        binding.userSwitcher.setOnClickListener {
            binding.spUser.performClick()
        }
        binding.spUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val gender = parent.getItemAtPosition(position).toString()
                binding.tvName.text = gender
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val layoutManager = GridLayoutManager(context, 2)
        val adapter = ReportAdapter(activity, dashboardItems)
        binding.rvReport.layoutManager = layoutManager
        binding.rvReport.setHasFixedSize(true)
        binding.rvReport.adapter = adapter

        //item spacing Decoration
        val spacing = resources.getDimensionPixelSize(R.dimen.dp_16)
        binding.rvReport.addItemDecoration(ItemSpacingHomeCards(spacing))

    }

    private fun initObserver() {
        viewModel.blogsApiResponse.observe(viewLifecycleOwner) {

            val layoutManager1 = GridLayoutManager(context, 2)
            val blogAdapter = DashboardBlogAdapter(activity, it.data)
            binding.rvBlogs.layoutManager = layoutManager1
            binding.rvBlogs.setHasFixedSize(true)
            binding.rvBlogs.adapter = blogAdapter
        }
    }

    fun setCustomText() {
        val context = binding.root.context
        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Resources\n")
        val firstFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular) })
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString("& Blogs")
        val secondFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_semibold) })
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder.append(firstText)
        builder.append(secondText)

//        binding.tvResend.movementMethod = LinkMovementMethod.getInstance()
        binding.tvResource.text = builder
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}