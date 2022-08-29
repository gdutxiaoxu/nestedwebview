package com.xj.webview.sample.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior

open class DisableAbleAppBarLayoutBehavior : Behavior {

    private val TAG = "DisableAbleAppBarLayout"

    // 设置是否可以下滑展开
    var isEnabled = true

    var iNestedScroll: INestedScroll? = null

    constructor() : super() {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }


    private fun init() {
        setDragCallback(object : DragCallback() {
            override fun canDrag(p0: AppBarLayout): Boolean {
                return isEnabled
            }

        })
    }


    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return isEnabled && super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        Log.d(
            TAG,
            "onNestedPreScroll before: dy is $dy,consumed[0] is ${consumed[0]},consumed[1] is ${consumed[1]}"
        )
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        Log.d(
            TAG,
            "onNestedPreScroll after: dy is $dy,consumed[0] is ${consumed[0]},consumed[1] is ${consumed[1]}"
        )
        if (isEnabled) {
            iNestedScroll?.onNestedPreScroll(dx, dy, consumed, type)
        }


    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        Log.d(TAG, "onNestedPreScroll: dy is $dyUnconsumed,dxConsumed is $dxConsumed")
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type
        )
    }

}