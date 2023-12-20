package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentQuestionsBinding
import com.flynaut.healthtag.model.response.QuestionAnswer
import com.flynaut.healthtag.model.response.QuestionResponse
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.QuestionViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

private const val ARG_CATEGORY_ID = "arg_category_id"

class QuestionFragment : BaseFragment<FragmentQuestionsBinding>() {

    private var categoryId: String? = null
    private var adapter: TestPagerAdapter? = null
    private var response: QuestionResponse? = null
    private lateinit var viewModel : QuestionViewModel
    private var isNextButtonEnabled = false
    var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getString(ARG_CATEGORY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[QuestionViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.ivBack.setOnClickListener{
////            (activity as MainActivity).onBackPressed()
//            parentFragment?.let { fragment ->
//                if (fragment is QuestionFragment) {
//                    (activity as MainActivity).onBackPressed()
//                }
//            }
//        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_questions
    }

    fun setNextButtonEnabled(enabled: Boolean) {
        isNextButtonEnabled = enabled
//        binding.btnNext.isEnabled = enabled

    }

    override fun initViews() {
        initObserver()
        categoryId?.let {
            showProgressDialog()
            viewModel.getQuestions(it)
        }

        setNextButtonEnabled(isNextButtonEnabled)

        binding.btnNext.setOnClickListener{
            if (!isNextButtonEnabled){
                Toast.makeText(requireContext(), "Please select an option to continue", Toast.LENGTH_SHORT).show()
            }else{
                val currentPage = binding.viewPager.currentItem
                val totalPage = adapter?.itemCount ?: 0
                if (currentPage == totalPage - 1) {
                    if (response?.isAllQuestionAnswered() == true) {
                        (activity as MainActivity).replaceFragment(ResultFragment.newInstance(
                            categoryId.toString(), response?.getTotalResultValue()!!
                        ),
                            "ResultFragment")
                    }
                } else if (currentPage < totalPage) {
                    binding.viewPager.setCurrentItem(currentPage + 1, true)
                    if (currentPage == totalPage - 2) {
                        binding.btnNext.text = "Submit"
                    }
                }
            }

        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                setNextButtonEnabled(false)
            }
        })

        val customPageChangeCallback = CustomPageChangeCallback(binding.viewPager) { isNextButtonEnabled }
        binding.viewPager.registerOnPageChangeCallback(customPageChangeCallback)

    }

    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            hideProgressDialog()
            this.response = response
            adapter = activity?.let { TestPagerAdapter(it, response.data,this) }
            binding.viewPager.adapter = adapter
            binding.indicator.setViewPager(binding.viewPager)
            binding.clQuestions.visibility = View.VISIBLE

        }
        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it)
        })
    }
    companion object {
        @JvmStatic
        fun newInstance(categoryId: String) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_ID, categoryId)
                }
            }
    }
}

class CustomPageChangeCallback(private val viewPager: ViewPager2, private val isNextButtonEnabled: () -> Boolean) :
    ViewPager2.OnPageChangeCallback() {

    override fun onPageSelected(position: Int) {
        if (isNextButtonEnabled()) {
            viewPager.isUserInputEnabled = true
        } else {
            viewPager.isUserInputEnabled = false
            viewPager.post {
                viewPager.currentItem = position
            }
        }
    }
}

class TestPagerAdapter(fragmentActivity: FragmentActivity, private var items: List<QuestionAnswer>, private val parentFragment: QuestionFragment) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun createFragment(position: Int): Fragment {
        return TestListFragment(items[position], position+1, items.count(), parentFragment)
    }

}

