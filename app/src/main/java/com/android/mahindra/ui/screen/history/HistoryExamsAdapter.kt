package com.android.mahindra.ui.screen.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.ui.screen.start_test.StartTestActivity
import kotlinx.android.synthetic.main.item_rv_history.view.*
import kotlinx.android.synthetic.main.item_rv_upcoming.view.*
import kotlinx.android.synthetic.main.item_rv_upcoming.view.duration
import kotlinx.android.synthetic.main.item_rv_upcoming.view.schedule
import kotlinx.android.synthetic.main.item_rv_upcoming.view.test_name


class HistoryExamsAdapter(val listHistoryFrag: List<ExamsModel>, val context: Context?) :
    RecyclerView.Adapter<HistoryExamsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_history, parent, false))

        return view
    }

    override fun getItemCount(): Int = listHistoryFrag.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            val item = listHistoryFrag[position]
            testName?.text = item.testName
            duration?.text = "${item.testDuration} mins"
            scheduledOn?.text = item.fromDate
            completedIn?.text = item.fromDate
            root?.setOnClickListener {
                val intent = Intent(it.context, StartTestActivity::class.java).apply {
                    putExtra("item", item)
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
        val completedIn = view.complete_in
    }
}
