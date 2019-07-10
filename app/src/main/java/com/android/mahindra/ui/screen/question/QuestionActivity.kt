package com.android.mahindra.ui.screen.question

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.databinding.ActivityQuestionBinding
import kotlinx.android.synthetic.main.activity_question.*
import org.jetbrains.anko.toast

class QuestionActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityQuestionBinding>(this, R.layout.activity_question)
    }
    private val viewModel by lazy {
        QuestionViewModel(this)
    }
    var countDownTimer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item = intent.getParcelableExtra<ExamsModel>("item")
        item?.let {
            val testId = it.testId.toString()
            initUiAndListeners(it.testDuration ?: "0")
            binding?.vm?.fetchData(testId)
        }

    }

    private fun initUiAndListeners(timeInMins: String) {
        supportActionBar?.title = "Questions"
        initToolBar()
        binding.vm = viewModel
        val timeToExpire = timeInMins.toLong() * 60 * 1000
        countDownTimer = object : CountDownTimer(timeToExpire, 1000) {
            override fun onFinish() {
                binding?.vm?.timeToExpire?.set("TimeOut")
            }

            override fun onTick(time: Long) {
                var rem = time / 1000

                val h = rem / (60 * 60)
                rem %= (60 * 60)

                val m = rem / 60
                rem %= 60

                binding?.vm?.timeToExpire?.set(
                    "${if (h < 10) "0$h" else h}:" +
                            "${if (m < 10) "0$m" else m}:" +
                            "${if (rem < 10) "0$rem" else rem}"
                )
            }
        }
        countDownTimer?.start()

        binding?.vm?.apply {

            setViewDisabled(previous)
        }
    }

    fun initViewPager() {
        binding?.vm?.apply {
            val quesAdapter = QuestionAdapter(questionList, supportFragmentManager)
            viewPager?.apply {
                adapter = quesAdapter
                previous?.setOnClickListener {
                    if (currentItem > 0)
                        currentItem -= 1
                }
                next?.setOnClickListener {
                    if (currentItem < questionList.size)
                        currentItem += 1
                }
            }
        }
    }


    fun setViewDisabled(view: View) {
        view.apply {
            isEnabled = false
            setBackgroundColor(ContextCompat.getColor(this@QuestionActivity, R.color.material_color_red200))
        }
    }

    fun setViewEnabled(view: View) {
        view.apply {
            isEnabled = true
            setBackgroundColor(ContextCompat.getColor(this@QuestionActivity, R.color.colorPrimary))
        }
    }

    fun showToast(msg: String) {
        toast(msg)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
        countDownTimer?.cancel()
    }

    private fun initToolBar() {
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar)
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        //    toolbar.setNavigationOnClickListener { onBackPressed() }
    }

}
