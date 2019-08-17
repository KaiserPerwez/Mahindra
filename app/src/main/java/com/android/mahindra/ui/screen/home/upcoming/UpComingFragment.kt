package com.android.mahindra.ui.screen.home.upcoming


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.FragmentUpComingBinding
import kotlinx.android.synthetic.main.fragment_up_coming.*

class UpComingFragment : androidx.fragment.app.Fragment() {
    private lateinit var listUpcomingFrag: List<ExamsModel>
    private lateinit var loginData: UserLoginData
    private lateinit var binding: FragmentUpComingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpComingBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpcomingExams()
    }

    fun setUpData(listUpcoming: List<ExamsModel>, userLoginData: UserLoginData) {
        listUpcomingFrag = listUpcoming
        loginData = userLoginData
    }

    private fun setUpcomingExams() {
        binding?.rvUpcoming?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = UpcomingExamsAdapter(listUpcomingFrag, this@UpComingFragment.context, loginData)
        }
    }

}
