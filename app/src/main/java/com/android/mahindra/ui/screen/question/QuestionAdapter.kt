package com.android.mahindra.ui.screen.question

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.mahindra.data.model.api.Question

class QuestionAdapter(val questionList: List<Question>, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    val fragList = mutableListOf<QuestionFrag>()
    override fun getItem(position: Int): Fragment {
        val frag = QuestionFrag(questionList[position])
        fragList.add(frag)
        return frag
    }

    override fun getCount(): Int = questionList.size
}