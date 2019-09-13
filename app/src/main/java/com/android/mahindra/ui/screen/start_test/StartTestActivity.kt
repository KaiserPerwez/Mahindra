package com.android.mahindra.ui.screen.start_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivityStartTestBinding
import com.android.mahindra.ui.screen.question.QuestionActivity
import com.android.mahindra.util.KEY_INTENT_EXAM_MODEL
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity


class StartTestActivity : AppCompatActivity() {
    private lateinit var item: ExamsModel
    private lateinit var userData: UserLoginData
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityStartTestBinding>(this, R.layout.activity_start_test)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        initToolBar()
        item = intent.getParcelableExtra<ExamsModel>(KEY_INTENT_EXAM_MODEL)
        userData = intent.getParcelableExtra<UserLoginData>(KEY_INTENT_LOGIN_DATA)

        binding?.apply {

            testDuration?.text = "1. Test duration ${item.testDuration} min"

            startTest?.setOnClickListener {

                alert(
                    "Your camera will be used to click random multiple photographs during the " +
                            "assessment for the purpose of authenticity check."
                ) {
                    positiveButton("Allow Camera") {
                        startTest()
                    }
                    negativeButton("Decline") { }
                }.show()
            }
        }
    }

    private fun startTest() {
        startActivity<QuestionActivity>(KEY_INTENT_EXAM_MODEL to item, KEY_INTENT_LOGIN_DATA to userData)
        finish()
    }

    private fun initToolBar() {
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
