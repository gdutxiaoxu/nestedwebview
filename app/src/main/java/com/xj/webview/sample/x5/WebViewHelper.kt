package com.xj.webview.sample.x5

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.*

import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class WebViewHelper(val mWebView: WebView, val context: Activity) {

    private val TAG = "WebViewHelper"


    private val FILE_CHOOSER_REQUEST = 100

    private var mClickBackTime: Long = 0

    private val mHomeUrl = "file:///android_asset/webpage/homePage.html"

    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

    private var mGeolocationCallback: GeolocationPermissionsCallback? = null
    private var locationPermissionUrl: String? = null

    private val mUrlEditText: EditText? = null


    fun initWebView() {
        val context: Context = this.context!!
        val webSetting: WebSettings = mWebView.getSettings()
        webSetting.javaScriptEnabled = true
        webSetting.allowFileAccess = true
        webSetting.setSupportZoom(true)
        webSetting.databaseEnabled = true
        webSetting.allowFileAccess = true
        webSetting.domStorageEnabled = true
        initWebViewClient()
        initWebChromeClient()


    }


    private fun initWebViewClient() {
        mWebView.setWebViewClient(object : WebViewClient() {
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
    }

    private fun initWebChromeClient() {
        val context: Context = this.context
        val activity: Activity = this.context
        mWebView.setWebChromeClient(object : WebChromeClient() {
            /**
             * 具体接口使用细节请参考文档：
             * https://x5.tencent.com/docs/webview.html
             * 或 Android WebKit 官方：
             * https://developer.android.com/reference/android/webkit/WebChromeClient
             *
             *
             */
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                Log.i(TAG, "onProgressChanged, newProgress:$newProgress, view:$view")
            }

            override fun onJsAlert(
                webView: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                AlertDialog.Builder(context).setTitle("JS弹窗Override")
                    .setMessage(message)
                    .setPositiveButton(
                        "OK"
                    ) { dialogInterface: DialogInterface?, i: Int -> result.confirm() }
                    .setCancelable(false)
                    .show()
                return true
            }

            override fun onJsConfirm(
                webView: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                AlertDialog.Builder(context).setTitle("JS弹窗Override")
                    .setMessage(message)
                    .setPositiveButton(
                        "OK"
                    ) { dialogInterface: DialogInterface?, i: Int -> result.confirm() }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialogInterface: DialogInterface?, i: Int -> result.cancel() }
                    .setCancelable(false)
                    .show()
                return true
            }

            override fun onJsBeforeUnload(
                webView: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                AlertDialog.Builder(context).setTitle("页面即将跳转")
                    .setMessage(message)
                    .setPositiveButton(
                        "OK"
                    ) { dialogInterface: DialogInterface?, i: Int -> result.confirm() }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialogInterface: DialogInterface?, i: Int -> result.cancel() }
                    .setCancelable(false)
                    .show()
                return true
            }

            override fun onJsPrompt(
                webView: WebView,
                url: String,
                message: String,
                defaultValue: String,
                result: JsPromptResult
            ): Boolean {
                val input = EditText(context)
                input.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                AlertDialog.Builder(context).setTitle("JS弹窗Override")
                    .setMessage(message)
                    .setView(input)
                    .setPositiveButton(
                        "OK"
                    ) { dialogInterface: DialogInterface?, i: Int ->
                        result.confirm(
                            input.text.toString()
                        )
                    }
                    .setCancelable(false)
                    .show()
                return true
            }

            /**
             * Return value usage see FILE_CHOOSE_REQUEST in
             * [BaseWebViewActivity.onActivityResult]
             */
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                Log.i(TAG, "openFileChooser: " + fileChooserParams.mode)
                mFilePathCallback = filePathCallback
                openFileChooseProcess(fileChooserParams.mode == FileChooserParams.MODE_OPEN_MULTIPLE)
                return true
            }

            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                geolocationPermissionsCallback: GeolocationPermissionsCallback
            ) {
                if (PermissionUtil.verifyLocationPermissions(activity)) {
                    geolocationPermissionsCallback.invoke(origin, true, false)
                } else {
                    locationPermissionUrl = origin
                    mGeolocationCallback = geolocationPermissionsCallback
                }
            }


        })
    }


    /* Don't care about the Base UI Logic below ^_^ */
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionUtil.REQUEST_EXTERNAL_STORAGE) {
            initWebView()
        }
        if (mGeolocationCallback != null && requestCode == PermissionUtil.REQUEST_GEOLOCATION) {
            val allow = grantResults[0] == PackageManager.PERMISSION_GRANTED
            mGeolocationCallback?.invoke(locationPermissionUrl, allow, false)
            mGeolocationCallback = null
            locationPermissionUrl = ""
        }
    }

    protected fun onDestroy() {
        if (mWebView != null) {
            mWebView.destroy()
        }
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack()
                return true
            }
            val currentTime = System.currentTimeMillis()
            // 3秒内连按两次后退按钮，退出应用
            if (currentTime - mClickBackTime < 3000) {
            } else {
                Toast.makeText(context.getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT)
                    .show()
                mClickBackTime = currentTime
            }
            return true
        }
        return false
    }

    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (mFilePathCallback != null) {
                    if (data != null && data.clipData != null) {
                        //有选择多个文件
                        val count = data.clipData!!.itemCount
                        Log.i(TAG, "url count ：  $count")
                        val uris = arrayOfNulls<Uri>(count)
                        var currentItem = 0
                        while (currentItem < count) {
                            val fileUri = data.clipData!!.getItemAt(currentItem).uri
                            uris[currentItem] = fileUri
                            currentItem = currentItem + 1
                        }
                        mFilePathCallback?.onReceiveValue(uris as? Array<Uri>)
                    } else {
                        val result = data?.data!!
                        Log.e(TAG, "" + result)
                        mFilePathCallback?.onReceiveValue(arrayOf(result))
                    }
                    mFilePathCallback = null
                }
            }
        } else {
//            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//            if (result != null) {
//                if (result.contents == null) {
//                    Toast.makeText(context, "扫描结果为空", Toast.LENGTH_SHORT).show()
//                } else {
//                    val str = result.contents
//                    if (mWebView != null) {
//                        mWebView.loadUrl(str)
//                    }
//                }
//            }
        }
    }

    private fun openFileChooseProcess(isMulti: Boolean) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.type = "*/*"
        if (isMulti) {
            Log.e(TAG, "putExtra")
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        context.startActivityForResult(
            Intent.createChooser(intent, "FileChooser"),
            FILE_CHOOSER_REQUEST
        )
    }

}