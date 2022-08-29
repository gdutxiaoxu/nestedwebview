package com.xj.webview.sample.x5

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.xj.webview.sample.R
import com.xj.webview.sample.behavior.DisableAbleAppBarLayoutBehavior
import com.xj.webview.sample.behavior.INestedScroll
import io.github.gdutxiaoxu.nestedwebview.x5.X5CallBackClient
import io.github.gdutxiaoxu.nestedwebview.x5.X5ProxyWebViewClientExtension

import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 *  博客地址： https://blog.csdn.net/gdutxiaoxu
 *  公众号： 徐公
 */
class NestedX5WebFragment : Fragment() {

    var iX5WebListener: IX5WebListener? = null

    private val TAG = "NestedX5WebFragment"

    private var url: String? = null
    private var param2: String? = null
    private lateinit var webView: WebView
    private lateinit var appBarLayout: AppBarLayout

    var iNestedScroll: INestedScroll? = null

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
        Log.i(TAG, "onCreateView: ")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_x5_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        webView = view.findViewById<WebView>(R.id.webview)
        val childCount = webView.childCount
        Log.i(TAG, "onViewCreated: webView  is $webView, childCount is $childCount")

        for (i in 0 until childCount) {
            Log.i(TAG, "x5 webView: childView[$i]  is ${webView.getChildAt(i)}")
        }

        appBarLayout = view.findViewById(R.id.appBarLayout)
        val layoutParams = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        val disableAbleAppBarLayoutBehavior =
            layoutParams.behavior as DisableAbleAppBarLayoutBehavior
        disableAbleAppBarLayoutBehavior.iNestedScroll =
            object : INestedScroll {
                override fun onNestedPreScroll(
                    dx: Int,
                    dy: Int,
                    consumed: IntArray,
                    type: Int
                ) {
                    iNestedScroll?.onNestedPreScroll(
                        dx,
                        dy,
                        consumed,
                        type
                    )
                }

            }
        Log.i(TAG, "onViewCreated: url is $url")
        val webSetting: WebSettings = webView.getSettings()
        webSetting.javaScriptEnabled = true
        webSetting.allowFileAccess = true
        webSetting.setSupportZoom(true)
        webSetting.databaseEnabled = true
        webSetting.allowFileAccess = true
        webSetting.domStorageEnabled = true
        webView.setWebViewClient(object : WebViewClient() {
            /**
             * 具体接口使用细节请参考文档：
             * https://x5.tencent.com/docs/webview.html
             * 或 Android WebKit 官方：
             * https://developer.android.com/reference/android/webkit/WebChromeClient
             */
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                Log.i(TAG, "onPageStarted, view:$view, url:$url")
            }

            override fun onPageFinished(view: WebView, url: String?) {
                Log.i(TAG, "onPageFinished, view:$view, url:$url")

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

            override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                Log.i(TAG, "shouldOverrideUrlLoading, view:$webView, url:$url")
                return super.shouldOverrideUrlLoading(webView, url)
            }

            override fun shouldOverrideUrlLoading(
                webView: WebView,
                webResourceRequest: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(webView, webResourceRequest)
            }

            override fun shouldInterceptRequest(
                webView: WebView?,
                webResourceRequest: WebResourceRequest?
            ): WebResourceResponse? {
                return if (webResourceRequest?.url.toString().contains("debugdebug")) {
                    var `in`: InputStream? = null
                    Log.i("AterDebug", "shouldInterceptRequest")
                    try {
                        `in` = FileInputStream(File("/sdcard/1.png"))
                    } catch (e: Exception) {
                    }
                    WebResourceResponse("image/*", "utf-8", `in`)
                } else {
                    super.shouldInterceptRequest(webView, webResourceRequest)
                }
            }
        })
        webView.loadUrl(url)
        val callbackClient =
            X5CallBackClient(
                webView.view,
                webView
            )

//        webView.webViewClientExtension = X5ProxyWebViewClientExtension(callbackClient)
//        webView.setWebViewCallbackClient(callbackClient)
    }

    fun checkGoBack() {
        if (canGoBack()) {
            webView.goBack()
        }
    }

    fun canGoBack() = webView.canGoBack()
    fun handleGoBack(): Boolean {
        val canGoBack = canGoBack()
        if (canGoBack){
            webView.goBack()
        }
        return canGoBack
    }


    companion object {
        private const val ARG_URL = "arg_url"
        private const val ARG_PARAM2 = "param2"


        @JvmStatic
        fun newInstance(url: String) =
            NestedX5WebFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
    }
}