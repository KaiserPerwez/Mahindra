package com.android.mahindra.ui.screen.home.history


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData

class HistoryFragment : androidx.fragment.app.Fragment() {
   private lateinit var listHistoryFrag: List<ExamsModel>
   private lateinit var loginData: UserLoginData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }
    fun setUpData(listHistory: List<ExamsModel>, userLoginData: UserLoginData) {
        listHistoryFrag = listHistory
        loginData = userLoginData
    }


}
