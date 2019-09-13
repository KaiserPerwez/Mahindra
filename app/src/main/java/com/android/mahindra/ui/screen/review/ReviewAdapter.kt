package com.android.mahindra.ui.screen.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.Question
import com.android.mahindra.ui.screen.question.QuestionActivity
import kotlinx.android.synthetic.main.item_rv_review.view.*

class ReviewAdapter(val listHistoryFrag: List<Question>, val activity: QuestionActivity) :
    RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_rv_review, parent, false))

        return view
    }

    override fun getItemCount(): Int = listHistoryFrag.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.questionNumber?.apply {
            val item = listHistoryFrag[position]
            text = "Q${position + 1}"

            backgroundTintList = if (item.statusReview == "1") {
                resources.getColorStateList(R.color.yellow_active)
            } else if (item.answer == "0000" || item.answer == "") {
                resources.getColorStateList(R.color.red_active)
            } else {
                resources.getColorStateList(R.color.green_active)
            }

            setOnClickListener {
                activity.binding?.viewPager?.currentItem = position
                activity.setQuestionOnUi(position)
                activity.dialog.dismiss()
            }
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val root = view.layout
        val questionNumber = view.question_number
    }
}
