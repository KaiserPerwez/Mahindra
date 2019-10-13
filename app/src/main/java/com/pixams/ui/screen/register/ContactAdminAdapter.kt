package com.pixams.ui.screen.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pixams.R
import com.pixams.data.model.api.Option
import com.pixams.databinding.ItemRvAdminOptionBinding
import com.pixams.ui.base.BaseRecyclerAdapter


class ContactAdminAdapter(val listOption: List<Option>, val activity: RegisterActivity) :
    BaseRecyclerAdapter<Option>(listOption) {


    lateinit var binding: ItemRvAdminOptionBinding
    val selectedChoiceList = mutableListOf<Option>()

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
                binding.apply {
                    checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
                        //        item?.isChecked = isChecked

                        if (isChecked)
                            selectedChoiceList.add(item)
                        else
                            selectedChoiceList.remove(item)
                    }
                }
            }
        }
    }
}
