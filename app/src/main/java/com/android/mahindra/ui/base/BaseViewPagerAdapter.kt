package com.android.mahindra.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class BaseViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val mFragmentList = mutableListOf<Fragment>()
    //  private val mFragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        //  mFragmentTitleList.add(title)
    }

    /* override fun getPageTitle(position: Int): CharSequence? {
         return mFragmentTitleList[position]
     }*/
}
