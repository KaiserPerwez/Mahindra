package com.android.mahindra.ui.screen.upcoming

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.ui.screen.start_test.StartTestActivity
import kotlinx.android.synthetic.main.item_rv_upcoming.view.*


class UpcomingExamsAdapter(val listUpcomingFrag: List<ExamsModel>, val context: Context?, val userLoginData: UserLoginData) :
    RecyclerView.Adapter<UpcomingExamsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_upcoming, parent, false))

        return view
    }

    override fun getItemCount(): Int = listUpcomingFrag.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            val item = listUpcomingFrag[position]
            testName?.text = item.testName
            duration?.text = "${item.testDuration} mins"
            scheduledOn?.text = item.fromDate
            root?.setOnClickListener {
                val intent = Intent(it.context, StartTestActivity::class.java).apply {
                    putExtra("item", item)
                    putExtra("user_data", userLoginData)
                }
                it.context.startActivity(intent)
            }
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val root = view.layout
        val testName = view.test_name
        val duration = view.duration
        val scheduledOn = view.schedule
    }
}
