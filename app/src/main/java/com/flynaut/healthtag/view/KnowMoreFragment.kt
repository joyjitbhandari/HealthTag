package com.flynaut.healthtag.view

import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentDevicesBinding
import com.flynaut.healthtag.databinding.FragmentKnowMoreLayoutBinding
import com.flynaut.healthtag.model.response.DataItemFaqDetail


class KnowMoreFragment : BaseFragment<FragmentKnowMoreLayoutBinding>() {
    private lateinit var dataModel: DataItemFaqDetail
    override fun getLayoutResId(): Int {
        return R.layout.fragment_know_more_layout
    }

    override fun initViews() {
        setClick()
        binding.tvQuestion.text=dataModel.title
        binding.tvDesc.text=dataModel.content
    }

    private fun setClick() {
        binding.ivBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(model: DataItemFaqDetail) =
            KnowMoreFragment().apply {
         dataModel=model
            }
    }
}