package com.xj.webview.sample.simple

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.xj.webview.sample.R
import com.xj.webview.sample.x5.IX5WebListener


class SimpleWebFragment : Fragment() {

    var iX5WebListener: IX5WebListener? = null

    private val TAG = "X5WebFragment"

    private var url: String? = null
    private var param2: String? = null
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
            param2 = it.getString(ARG_PARAM2)
        }
        url = url ?: "https://ds.163.com/"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.simple_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val smartRefreshLayout = view.findViewById<SmartRefreshLayout>(R.id.refresh_layout)
//        smartRefreshLayout.setOnRefreshListener(object : OnRefreshListener {
//            override fun onRefresh(refreshLayout: RefreshLayout) {
//                Log.i(TAG, "onRefresh: ")
//            }
//
//        })
//        smartRefreshLayout.setEnableRefresh(false)
//        smartRefreshLayout.setEnableLoadMore(false)
        webView = view.findViewById<WebView>(R.id.webview)
        val webSetting: WebSettings = webView.getSettings()
        webSetting.javaScriptEnabled = true
        webSetting.allowFileAccess = true
        webSetting.setSupportZoom(true)
        webSetting.databaseEnabled = true
        webSetting.allowFileAccess = true
        webSetting.domStorageEnabled = true
        webView.loadUrl(url!!)
        webView.webViewClient = object : WebViewClient() {
            override fun doUpdateVisitedHistory(p0: WebView?, url: String?, p2: Boolean) {
                super.doUpdateVisitedHistory(p0, url, p2)
                iX5WebListener?.canGoBack(p0?.canGoBack() ?: false)
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
            }


            override fun onReceivedError(
                webView: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Log.e(
                    TAG, "onReceivedError: " + errorCode
                            + ", description: " + description
                            + ", url: " + failingUrl
                )
            }
        }

    }

    fun checkGoBack() {
        if (canGoBack()) {
            webView.goBack()
        }
    }

    fun canGoBack() = webView.canGoBack()

    companion object {
        private const val ARG_URL = "arg_url"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(url: String) =
            SimpleWebFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
    }
}