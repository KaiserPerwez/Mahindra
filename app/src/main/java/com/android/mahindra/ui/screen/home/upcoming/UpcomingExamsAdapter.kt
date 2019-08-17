package com.android.mahindra.ui.screen.home.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ItemRvUpcomingBinding
import com.android.mahindra.ui.screen.start_test.StartTestActivity
import com.android.mahindra.util.KEY_INTENT_EXAM_MODEL
import com.android.mahindra.util.KEY_INTENT_EXAM_USER
import org.jetbrains.anko.startActivity


class UpcomingExamsAdapter(
    val listUpcomingFrag: List<ExamsModel>,
    val context: Context?,
    val userLoginData: UserLoginData
) :
    RecyclerView.Adapter<UpcomingExamsAdapter.MyViewHolder>() {
    lateinit var binding: ItemRvUpcomingBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //val view = MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_upcoming, parent, false))
        binding = ItemRvUpcomingBinding.inflate(LayoutInflater.from(context))
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listUpcomingFrag.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(listUpcomingFrag[position])

    inner class MyViewHolder(val binding: ItemRvUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExamsModel) {
            binding.apply {
                model = item
                root?.setOnClickListener {
                    it.context.startActivity<StartTestActivity>(KEY_INTENT_EXAM_MODEL to item, KEY_INTENT_EXAM_USER to userLoginData)
                }
            }
        }
    }
}
