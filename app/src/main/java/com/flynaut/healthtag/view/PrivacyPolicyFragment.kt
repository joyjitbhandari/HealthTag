package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentPrivacyPolicyBinding

private const val ARG_TITLE = "title"
private const val ARG_Link = "link"
class PrivacyPolicyFragment : BaseFragment<FragmentPrivacyPolicyBinding>() {

    private var title: String? = null
    private var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            link = it.getString(ARG_Link)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_privacy_policy
    }

    override fun initViews() {
        binding.tvScreenTitle.text = title
        binding.webLink.settings.javaScriptEnabled = true
        binding.webLink.settings.domStorageEnabled = true
        binding.webLink.settings.loadWithOverviewMode = true
        binding.webLink.settings.useWideViewPort = true

        binding.progressBarLayout.visibility = View.VISIBLE

        binding.webLink.webChromeClient = WebChromeClient()
        binding.webLink.loadUrl(link ?: "")

        binding.webLink.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {
                    binding.progressBarLayout.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, link: String) =
            PrivacyPolicyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_Link, link)
                }
            }
    }

}