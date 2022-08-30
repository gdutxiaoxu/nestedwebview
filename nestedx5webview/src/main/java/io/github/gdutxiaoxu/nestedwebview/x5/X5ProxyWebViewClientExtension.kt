package io.github.gdutxiaoxu.nestedwebview.x5

import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension
import com.tencent.smtt.sdk.WebViewCallbackClient

/**
 *  博客地址： https://blog.csdn.net/gdutxiaoxu
 *  公众号： 徐公
 */
class X5ProxyWebViewClientExtension(var mCallbackClient: WebViewCallbackClient) :
    ProxyWebViewClientExtension() {


    private val TAG = "X5_ProxyWebViewClientExt"

    override fun invalidate() {}
    override fun onReceivedViewSource(data: String) {}
    override fun onTouchEvent(event: MotionEvent, view: View): Boolean {
        return mCallbackClient.onTouchEvent(event, view)
    }

    // 1
    override fun onInterceptTouchEvent(ev: MotionEvent, view: View): Boolean {
        return mCallbackClient.onInterceptTouchEvent(ev, view)
    }

    // 3
    override fun dispatchTouchEvent(ev: MotionEvent, view: View): Boolean {
        return mCallbackClient.dispatchTouchEvent(ev, view)
    }

    // 4
    override fun overScrollBy(
        deltaX: Int, deltaY: Int, scrollX: Int, scrollY: Int,
        scrollRangeX: Int, scrollRangeY: Int,
        maxOverScrollX: Int, maxOverScrollY: Int,
        isTouchEvent: Boolean, view: View
    ): Boolean {

        Log.i(TAG, "overScrollBy: ")
//        return super.overScrollBy( deltaX, deltaY, scrollX, scrollY,
//            scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent, view)
        return mCallbackClient.overScrollBy(
            deltaX, deltaY, scrollX, scrollY,
            scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent, view
        )
    }

    // 5
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int, view: View) {

        mCallbackClient.onScrollChanged(l, t, oldl, oldt, view)
    }

    // 6
    override fun onOverScrolled(
        scrollX: Int, scrollY: Int, clampedX: Boolean,
        clampedY: Boolean, view: View
    ) {
        mCallbackClient.onOverScrolled(scrollX, scrollX, clampedX, clampedY, view)
    }

    // 7
    override fun computeScroll(view: View) {
        mCallbackClient.computeScroll(view)
    }
}

