package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentFullMediaLayoutBinding
import com.flynaut.healthtag.model.ModelChat

class FullScreenMediaFragment : BaseFragment<FragmentFullMediaLayoutBinding>() {
    private lateinit var model: ModelChat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_full_media_layout
    }

    override fun initViews() {
        Glide.with(binding.root.context)
            .load(model.text)
            .into(binding.photoView)
    }

    companion object {
        @JvmStatic
        fun newInstance(data: ModelChat) =
            FullScreenMediaFragment().apply {
                model = data
            }
    }

}