package com.android.mahindra.ui.screen.home

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.ui.screen.home.history.HistoryFragment
import com.android.mahindra.ui.screen.home.upcoming.UpComingFragment

class MyAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int,
    val list: List<ExamsModel>,
    val userLoginData: UserLoginData
) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): androidx.fragment.app.Fragment? {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                val listUpcoming = list.filter { it.status?.toLowerCase() != "completed" }
                return UpComingFragment().apply {
                    setUpData(listUpcoming,userLoginData)
                }
            }
            1 -> {
                val listHistory = list.filter { (it.status?.toLowerCase() == "completed") }
                return HistoryFragment().apply {
                    setUpData(listHistory,userLoginData)
                }
            }

            else -> return null
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}