package com.android.mahindra.Adapter

import android.support.v4.app.FragmentPagerAdapter
import android.content.Context;
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.android.mahindra.Fragment.HistoryFragment
import com.android.mahindra.Fragment.UpComingFragment

class MyAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return UpComingFragment()
            }
            1 -> {
                return HistoryFragment()
            }

            else -> return null
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}