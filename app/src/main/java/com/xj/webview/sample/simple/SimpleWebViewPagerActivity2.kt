package com.xj.webview.sample.simple

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.xj.webview.FragmentTab
import com.xj.webview.sample.BasePagerAdapter
import com.xj.webview.sample.R
import com.xj.webview.sample.x5.IX5WebListener


class SimpleWebViewPagerActivity2 : AppCompatActivity() {
    var mViewPager: ViewPager? = null

    private var mTabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_web2)

        // 第一步，初始化ViewPager和TabLayout
        mViewPager = findViewById<View>(R.id.viewpager) as ViewPager
        mTabLayout = findViewById<View>(R.id.tabs) as TabLayout
        setupViewPager()
    }

    private fun setupViewPager() {
        val arrayList = ArrayList<FragmentTab>()
        val fragment =
            SimpleWebFragment.newInstance("https://juejin.cn/user/2207475076966584/posts")
        fragment.iX5WebListener = object : IX5WebListener {
            override fun onClick() {
                TODO("Not yet implemented")
            }

            override fun canGoBack(canGoBack: Boolean) {

            }

        }
        arrayList.add(
            FragmentTab("主页", fragment)
        )
        arrayList.add(
            FragmentTab(
                "github", SimpleWebFragment.newInstance("https://gdutxiaoxu.github.io/")
            )
        )
        // 第二步：为ViewPager设置适配器
        val adapter = BasePagerAdapter(supportFragmentManager, arrayList)
        mViewPager!!.adapter = adapter
        //  第三步：将ViewPager与TableLayout 绑定在一起
        mTabLayout!!.setupWithViewPager(mViewPager)
    }
}

fun View.setViewVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}