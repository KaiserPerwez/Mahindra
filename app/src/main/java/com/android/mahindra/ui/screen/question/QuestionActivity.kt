package com.android.mahindra.ui.screen.question

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
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
        val item = intent.getParcelableExtra<ExamsModel>("item")
        item?.let {
            val testId = it.testId.toString()
            initUiAndListeners(it.testDuration ?: "0")
            binding?.vm?.fetchData(testId)
        }

    }

    private fun initUiAndListeners(timeInMins: String) {
        supportActionBar?.title = "Questions"
        //initToolBar()

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


            previous?.setOnClickListener {
                val currentIndex = indexCurrentQuestion.get()!!.toInt() - 1
                val newIndex = currentIndex - 1

                if (newIndex >= 0) {
                    currentQuestion.set(questionList[newIndex])
                    indexCurrentQuestion?.set((newIndex + 1).toString())

                    when (questionList[newIndex].type) {
                        "audio" -> binding.tvAudio.text = answerList[newIndex].answer
                        "video" -> binding.tvVideo.text = answerList[newIndex].answer
                        "dropdown" -> binding.tvDropdown.text = answerList[newIndex].answer
                        "choice" -> {
                            binding.chkA.isChecked = answerList[newIndex].answer[0] == '1'
                            binding.chkB.isChecked = answerList[newIndex].answer[1] == '1'
                            binding.chkC.isChecked = answerList[newIndex].answer[2] == '1'
                            binding.chkD.isChecked = answerList[newIndex].answer[3] == '1'
                        }
                        "number" -> binding.txtNumber.text
                        "text" -> binding.txtText.text
                        else -> ""
                    }
                }
            }
            next?.setOnClickListener {

                val currentIndex = indexCurrentQuestion.get()!!.toInt() - 1
                val newIndex = currentIndex + 1

                answerList[currentIndex].answer =
                    when (answerList[currentIndex].quesnType) {
                        "audio" -> binding.tvAudio.text.let { if (it.startsWith("Click to")) "" else it }
                        "video" -> binding.tvVideo.text.let { if (it.startsWith("Click to")) "" else it }
                        "dropdown" -> binding.tvDropdown.text.let { if (it.startsWith("Click to")) "" else it }
                        "choice" -> (if (binding.chkA.isChecked) "1" else "0") +
                                (if (binding.chkB.isChecked) "1" else "0") +
                                (if (binding.chkC.isChecked) "1" else "0") +
                                (if (binding.chkD.isChecked) "1" else "0")
                        "number" -> binding.txtNumber.text
                        "text" -> binding.txtText.text
                        else -> ""
                    }.toString()



                if (questionList[currentIndex].mandatory!! &&
                    (answerList[currentIndex].answer.isBlank() || answerList[currentIndex].answer == "0000")
                ) {
                    showToast("Answering this question is mandatory")
                    return@setOnClickListener
                }



                if (newIndex < questionList.size) {
                    currentQuestion.set(questionList[newIndex])
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
        //  viewModel.onResume()
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
