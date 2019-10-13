package com.pixams.ui.screen.home.upcoming


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixams.data.model.api.ExamsModel
import com.pixams.data.model.api.UserLoginData
import com.pixams.data.remote.api.ApiService
import com.pixams.databinding.FragmentUpComingBinding
import io.reactivex.disposables.Disposable

class UpComingFragment : androidx.fragment.app.Fragment() {
    private lateinit var listUpcomingFrag: List<ExamsModel>
    private lateinit var loginData: UserLoginData
    private lateinit var binding: FragmentUpComingBinding

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

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
