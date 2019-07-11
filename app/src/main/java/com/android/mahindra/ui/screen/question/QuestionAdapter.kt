package com.android.mahindra.ui.screen.question

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.android.mahindra.data.model.api.Question

class QuestionAdapter(val questionList: List<Question>, fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = QuestionFrag(questionList[position])

    override fun getCount(): Int = questionList.size
}