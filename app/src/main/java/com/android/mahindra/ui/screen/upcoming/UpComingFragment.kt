package com.android.mahindra.ui.screen.upcoming


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import kotlinx.android.synthetic.main.fragment_up_coming.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UpComingFragment : androidx.fragment.app.Fragment() {
    lateinit var listUpcomingFrag: List<ExamsModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_up_coming, container, false)


      /*  view.layout.setOnClickListener() {
            val intent = Intent(activity, StartTestActivity::class.java)
            startActivity(intent)
        }*/

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpcomingExams()
    }

    private fun setUpcomingExams() {
        rv_upcoming?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = UpcomingExamsAdapter(listUpcomingFrag, this@UpComingFragment.context)
        }
    }

}
