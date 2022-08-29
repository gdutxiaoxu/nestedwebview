package com.xj.webview.sample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.jvm.JvmOverloads
import androidx.fragment.app.FragmentPagerAdapter
import com.xj.webview.FragmentTab


class BasePagerAdapter @JvmOverloads constructor(
    fm: FragmentManager,
    val fragmentTabList: MutableList<FragmentTab>,
) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        //        Logger.i("BaseFragmentAdapter position=" +position);
        return fragmentTabList[position].fragment
    }

    override fun getCount(): Int {
        return fragmentTabList.size
    }

    val isEmpty: Boolean
        get() = fragmentTabList.isEmpty()

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTabList[position].title
    } /*  @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }*/


}