package com.android.mahindra.ui.screen.home.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ItemRvUpcomingBinding
import com.android.mahindra.ui.base.BaseRecyclerAdapter
import com.android.mahindra.ui.screen.start_test.StartTestActivity
import com.android.mahindra.util.KEY_INTENT_EXAM_MODEL
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import org.jetbrains.anko.startActivity


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
                    it.context.startActivity<StartTestActivity>(
                        KEY_INTENT_EXAM_MODEL to item,
                        KEY_INTENT_LOGIN_DATA to userLoginData
                    )
                }
            }
        }
    }
}
