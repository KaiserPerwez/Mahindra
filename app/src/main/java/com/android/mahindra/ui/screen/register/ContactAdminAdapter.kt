package com.android.mahindra.ui.screen.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.Option
import com.android.mahindra.databinding.ItemRvAdminOptionBinding
import com.android.mahindra.ui.base.BaseRecyclerAdapter


class ContactAdminAdapter(val listOption: List<Option>, val activity: RegisterActivity) :
    BaseRecyclerAdapter<Option>(listOption) {


    lateinit var binding: ItemRvAdminOptionBinding

    override fun onCreateViewHolderBase(
        parent: ViewGroup?,
        viewType: Int
    ): MyViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.item_rv_admin_option,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolderBase(holder: RecyclerView.ViewHolder?, position: Int) =
        (holder as MyViewHolder).bind(listOption[position])

    inner class MyViewHolder(val binding: ItemRvAdminOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Option) {
            binding.apply {
                model = item
                root.setOnClickListener {
                    /*                    it.context.startActivity<StartTestActivity>(
                                                        KEY_INTENT_EXAM_MODEL to item,
                                                        KEY_INTENT_LOGIN_DATA to userLoginData
                                                    )*/
                }
            }
        }
    }
}
