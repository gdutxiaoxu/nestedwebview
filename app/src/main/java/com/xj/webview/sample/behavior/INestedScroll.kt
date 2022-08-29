package com.xj.webview.sample.behavior


interface INestedScroll {
    fun onNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    )
}