package com.android.mahindra.ui.screen.home.history


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.FragmentHistoryBinding

class HistoryFragment : androidx.fragment.app.Fragment() {
    private lateinit var listHistoryFrag: List<ExamsModel>
    private lateinit var loginData: UserLoginData
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater)
        return binding.root
      //  return inflater.inflate(R.layout.fragment_history, container, false)
    }

    fun setUpData(listHistory: List<ExamsModel>, userLoginData: UserLoginData) {
        listHistoryFrag = listHistory
        loginData = userLoginData
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHistoryExams()
    }

    private fun setHistoryExams() {
        binding?.rvHistory?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = HistoryExamsAdapter(listHistoryFrag, this@HistoryFragment.context)
        }
    }

}
