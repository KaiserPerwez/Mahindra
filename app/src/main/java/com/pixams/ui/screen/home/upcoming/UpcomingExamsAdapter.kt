package com.pixams.ui.screen.home.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pixams.R
import com.pixams.data.model.api.ExamsModel
import com.pixams.data.model.api.UserLoginData
import com.pixams.databinding.ItemRvUpcomingBinding
import com.pixams.ui.base.BaseRecyclerAdapter
import com.pixams.ui.screen.home.HomeActivity


class UpcomingExamsAdapter(
    val listUpcomingFrag: List<ExamsModel>,
    val context: Context?,
    val userLoginData: UserLoginData
) :
    BaseRecyclerAdapter<ExamsModel>(listUpcomingFrag) {


    lateinit var binding: ItemRvUpcomingBinding
    override fun onCreateViewHolderBase(parent: ViewGroup?, viewType: Int): MyViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_rv_upcoming,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolderBase(holder: RecyclerView.ViewHolder?, position: Int) =
        (holder as MyViewHolder).bind(listUpcomingFrag[position])


    inner class MyViewHolder(val binding: ItemRvUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExamsModel) {
            binding.apply {
                model = item
                root.setOnClickListener {
                    (context as HomeActivity).validateTest(item)
                }
            }
        }
    }
}
