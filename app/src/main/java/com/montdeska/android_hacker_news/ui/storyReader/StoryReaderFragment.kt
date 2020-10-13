package com.montdeska.android_hacker_news.ui.storyReader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.montdeska.android_hacker_news.R
import com.montdeska.android_hacker_news.base.BaseFragment
import kotlinx.android.synthetic.main.story_reader_fragment.*
import kotlinx.android.synthetic.main.story_reader_fragment.pullToRefresh

class StoryReaderFragment : BaseFragment() {
    companion object {
        fun newInstance() = StoryReaderFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.story_reader_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addRefreshListener()
        val url = arguments?.getString("storyUrl")
        pullToRefresh?.isRefreshing = true
        webView.webChromeClient = object : WebChromeClient() {}
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                pullToRefresh?.isRefreshing = false
            }

        }

        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url!!)
    }

    private fun addRefreshListener() {
        pullToRefresh.setColorSchemeResources(R.color.white)
        pullToRefresh.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark)
        pullToRefresh.setOnRefreshListener {
            webView.reload()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else {
            super.onBackPressed()
        }
    }
}