package com.flynaut.healthtag.view

//import com.flynaut.healthtag.model.items
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.ChooseCategoryAdapter
import com.flynaut.healthtag.databinding.FragmentChooseCategoryBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.CustomTypefaceSpan
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.setVisible
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.CategoryViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import kotlin.math.roundToInt


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ChooseCategoryFragment : BaseFragment<FragmentChooseCategoryBinding>() {

    // TODO: Rename and change types of parameters
    private var isOnboard = false
    private lateinit var viewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isOnboard = it.getBoolean(ARG_IS_ONBOARD)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isOnboard)
            binding.ivBack.setVisible(false)
        else
            binding.ivBack.setVisible(true)

        binding.ivBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[CategoryViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_choose_category
    }

    override fun initViews() {
        initObserver()
        showProgressDialog()
        viewModel.getCategory()
        binding.btnLogin.setOnClickListener {
            (activity as MainActivity).replaceFragment(LoginFragment(), "LoginFragment")
        }
        setCustomText()

    }

    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            hideProgressDialog()
            val adapter = context?.let { ChooseCategoryAdapter(it, activity, response.data) }
            binding.gridLayout.adapter = adapter

        }
        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it)

        })
    }

    fun setCustomText() {

        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Lorem Ipsum is\nsimply")
        val firstFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular) })
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            firstText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val secondText = SpannableString("dummy text?")
        val secondFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium) })
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.root.context.let {
            secondText.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        it,
                        R.color.black
                    )
                ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        builder.append(firstText)
        builder.append(" ")
        builder.append(secondText)

        binding.tvTitle.text = builder
    }


    private fun dpToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    companion object {
        @JvmStatic
        fun newInstance(isOnboard: Boolean) =
            ChooseCategoryFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_ONBOARD, isOnboard)
                }
            }
    }


}