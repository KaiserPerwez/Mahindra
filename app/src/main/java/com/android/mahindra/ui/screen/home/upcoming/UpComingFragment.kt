package com.android.mahindra.ui.screen.home.upcoming


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.Status
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.databinding.FragmentUpComingBinding
import com.android.mahindra.ui.screen.start_test.StartTestActivity
import com.android.mahindra.util.KEY_INTENT_EXAM_MODEL
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

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
