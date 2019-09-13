package com.android.mahindra.ui.screen.register

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.Option


class ContactAdminAdapter(val listOption: List<Option>?, val activity: RegisterActivity) :
    RecyclerView.Adapter<ContactAdminAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_rv_admin_option, parent, false))

        return view
    }

    override fun getItemCount(): Int = listOption.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*holder.questionNumber?.apply {
            val item = listOption[position]
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
        }*/
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val root = view.layout
//        val questionNumber = view.question_number
    }
}
