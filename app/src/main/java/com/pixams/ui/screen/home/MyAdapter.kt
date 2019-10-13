package com.pixams.ui.screen.home

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.pixams.data.model.api.ExamsModel
import com.pixams.data.model.api.UserLoginData
import com.pixams.ui.screen.home.history.HistoryFragment
import com.pixams.ui.screen.home.upcoming.UpComingFragment

class MyAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int,
    val list: List<ExamsModel>,
    val userLoginData: UserLoginData
) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    val upComingFragment by lazy {
        UpComingFragment()
    }
    val historyFragment by lazy {
        HistoryFragment()
    }

    // this is for fragment tabs
    override fun getItem(position: Int): androidx.fragment.app.Fragment? {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                val listUpcoming = list.filter { it.status?.toLowerCase() != "completed" }
                return upComingFragment.apply {
                    setUpData(listUpcoming, userLoginData)
                }
            }
            1 -> {
                val listHistory = list.filter { (it.status?.toLowerCase() == "completed") }
                return historyFragment.apply {
                    setUpData(listHistory, userLoginData)
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