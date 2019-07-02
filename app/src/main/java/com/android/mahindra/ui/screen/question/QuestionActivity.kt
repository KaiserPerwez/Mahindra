package com.android.mahindra.ui.screen.question

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.databinding.ActivityQuestionBinding
import kotlinx.android.synthetic.main.activity_question.*
import org.jetbrains.anko.selector
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
        initUiAndListeners()

        val testId = intent?.getStringExtra("testId") ?: "1"
        binding?.vm?.fetchData(testId)
    }

    private fun initUiAndListeners() {
        supportActionBar?.title = "Questions"
        //initToolBar()

        binding.vm = viewModel

        val timeToExpire = (intent?.getStringExtra("timer") ?: "120000").toLong()
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


            previous?.setOnClickListener {
                val currentIndex = indexCurrentQuestion.get()!!.toInt() - 1
                val newIndex = currentIndex - 1
                if (newIndex >= 0) {
                    currentQuestion.set(questionList.get(newIndex))
                    indexCurrentQuestion?.set((newIndex + 1).toString())
                }
            }
            next?.setOnClickListener {
                val currentIndex = indexCurrentQuestion.get()!!.toInt() - 1
                val newIndex = currentIndex + 1
                if (newIndex < questionList.size) {
                    currentQuestion.set(questionList.get(newIndex))
                    indexCurrentQuestion?.set((newIndex + 1).toString())
                }
            }
            tv_dropdown?.setOnClickListener {
                val options = currentQuestion.get()!!.options
                if (options.isNullOrEmpty()) return@setOnClickListener
                selector("Select an option", options) { dialogInterface, index ->
                    tv_dropdown?.text = options[index]
                }
            }
        }
    }

    fun showToast(msg: String) {
        toast(msg)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

}
