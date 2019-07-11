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
        binding?.model = item
        return binding!!.root
    }
}
