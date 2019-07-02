package com.android.mahindra.ui.screen.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import kotlinx.android.synthetic.main.item_rv_upcoming.view.*

class UpcomingExamsAdapter(val listUpcomingFrag: List<ExamsModel>, val context: Context?) :
    RecyclerView.Adapter<UpcomingExamsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_upcoming, parent, false))
    }

    override fun getItemCount(): Int = listUpcomingFrag.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder?.apply {
            val item=listUpcomingFrag[position]
            testName?.text=item.testName
            duration?.text=item.testDuration
            scheduledOn?.text=item.fromDate
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val testName = view.test_name
        val duration = view.duration
        val scheduledOn = view.schedule
    }
}
