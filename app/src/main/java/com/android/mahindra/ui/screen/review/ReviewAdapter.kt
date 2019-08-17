package com.android.mahindra.ui.screen.review

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.Question
import com.android.mahindra.ui.screen.start_test.StartTestActivity
import kotlinx.android.synthetic.main.item_rv_review.view.*
import kotlinx.android.synthetic.main.item_rv_upcoming.view.*


class ReviewAdapter(val listHistoryFrag: List<Question>, val context: Context?) :
    RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_review, parent, false))

        return view
    }

    override fun getItemCount(): Int = listHistoryFrag.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            val item = listHistoryFrag[position]
            questionNumber?.text = "Q${position + 1}"
            root?.setOnClickListener {
                val intent = Intent(it.context, Question::class.java).apply {
                    putExtra("item", item)
                }
                it.context.startActivity(intent)
            }
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val root = view.layout
        val questionNumber = view.question_number
    }
}
