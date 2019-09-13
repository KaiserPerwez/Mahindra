package com.android.mahindra.ui.screen.register

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.Option
import kotlinx.android.synthetic.main.item_rv_admin_option.view.*


class ContactAdminAdapter(val listOption: List<Option>?, val activity: RegisterActivity) :
    RecyclerView.Adapter<ContactAdminAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_rv_admin_option, parent, false))

        return view
    }

    override fun getItemCount(): Int = listOption?.size ?:0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title?.apply {
            val item = listOption?.get(position)
            text = item?.option

            /*setOnClickListener {
                activity.binding?.viewPager?.currentItem = position
                activity.setQuestionOnUi(position)
                activity.dialog.dismiss()
            }*/
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val root = view.rootView
        val title = view.title
//        val questionNumber = view.question_number
    }
}
