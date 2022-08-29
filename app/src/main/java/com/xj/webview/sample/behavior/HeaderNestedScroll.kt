package com.xj.webview.sample.behavior

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.math.abs

/**
 * 将 head 部分 和 content 部分进行便宜，这里 head 部分是 tab， content 部分额是 ViewPager
 */
open class HeaderNestedScroll(private val headContainer: View, private val contentContainer: View, lifecycleOwner: LifecycleOwner? = null) :
    INestedScroll {
    private val TAG = "HeaderNestedScroll"

    var isEnable = true

    var lastState = State.EXPANDED


    init {
        val viewTreeListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                val measuredHeight = headContainer.measuredHeight.toFloat()
//                Log.i(TAG, "onGlobalLayout: ${headContainer.translationY},measuredHeight is ${measuredHeight}")
                if (measuredHeight <= 0) {
                    return
                }
                val translationY = headContainer.translationY
                val state = getState(translationY, measuredHeight)
                if (lastState != state) {
                    handleStateChange(state)
                }
            }
        }

        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                Log.i(TAG, "onDestroy: ")
                headContainer.viewTreeObserver.removeOnGlobalLayoutListener(viewTreeListener)
            }

        })

        headContainer.viewTreeObserver.addOnGlobalLayoutListener(viewTreeListener)
    }

    private fun getState(translationY: Float, measuredHeight: Float): State {
        return if (translationY >= 0f) {
            State.EXPANDED
        } else if (translationY <= -measuredHeight) {
            State.COLLAPSED
        } else {
            State.INTERMEDIATE
        }
    }

    override fun onNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (!isEnable) {
            return
        }

        if (abs(dx) >= abs(dy)) {
            return
        }

        if (dy > 0) { // 向上滑动，隐藏
            val measuredHeight = headContainer.measuredHeight.toFloat()
            val translationY = headContainer.translationY
            val finalY = Math.max(translationY - dy, -measuredHeight)
            val isHide = finalY <= -measuredHeight
            headContainer.translationY = finalY
            contentContainer.translationY = finalY
            // 为了下拉刷新的时候，tab 不可见，所以设置不可见
            if (isHide) {
                headContainer.visibility = View.INVISIBLE
                handleStateChange(State.COLLAPSED)
            } else {
                handleStateChange(State.INTERMEDIATE)
            }
        } else { // 向下滑动，出现
            if (headContainer.translationY <= 0f) {
                val finalY = Math.min(headContainer.translationY - dy, 0f)
                headContainer.translationY = finalY
                contentContainer.translationY = finalY
                headContainer.visibility = View.VISIBLE
                if (finalY >= 0f) {
                    handleStateChange(State.EXPANDED)
                } else {
                    handleStateChange(State.INTERMEDIATE)

                }
            }
        }
    }

    private fun handleStateChange(state: State) {
        if (lastState != state) {
            Log.i(TAG, "onStateChange: state is $state, lastState is $lastState")
            onStateChange(lastState, state)
            lastState = state
        }
    }

    open fun onStateChange(lastState: State, state: State) {

    }

    enum class State {
        EXPANDED,
        COLLAPSED,
        INTERMEDIATE,
    }


}