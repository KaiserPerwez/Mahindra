package com.android.mahindra.Adapter

import android.content.Context;
import com.android.mahindra.ui.screen.history.HistoryFragment
import com.android.mahindra.ui.screen.upcoming.UpComingFragment

class MyAdapter(private val myContext: Context, fm: androidx.fragment.app.FragmentManager, internal var totalTabs: Int) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): androidx.fragment.app.Fragment? {
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