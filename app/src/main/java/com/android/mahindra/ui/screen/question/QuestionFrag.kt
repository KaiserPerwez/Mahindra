package com.android.mahindra.ui.screen.question


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.mahindra.R
import com.android.mahindra.data.model.api.Question
import com.android.mahindra.databinding.FragmentQuestionBinding


class QuestionFrag(val item: Question) : Fragment() {

    var binding: FragmentQuestionBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            model = item
            chkA?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    resetCheckBoxes()
                    chkA.isChecked = true
                }
            }
            chkB?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    resetCheckBoxes()
                    chkB.isChecked = true
                }
            }
            chkC?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    resetCheckBoxes()
                    chkC.isChecked = true
                }
            }
            chkD?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    resetCheckBoxes()
                    chkD.isChecked = true
                }
            }
        }

    }

    fun resetCheckBoxes() {
        binding?.apply {
            chkA?.isChecked = false
            chkB?.isChecked = false
            chkC?.isChecked = false
            chkD?.isChecked = false
        }
    }
}
