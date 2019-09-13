package com.android.mahindra.ui.screen.test_complete

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivityStartTestBinding
import com.android.mahindra.databinding.ActivityTestCompleteBinding
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.ui.screen.question.QuestionActivity
import com.android.mahindra.util.KEY_INTENT_EXAM_MODEL
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import kotlinx.android.synthetic.main.activity_start_test.*
import org.jetbrains.anko.*

class TestCompleteActivity : AppCompatActivity() {
    private lateinit var userData: UserLoginData
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityTestCompleteBinding>(this, R.layout.activity_test_complete)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        initToolBar()
        userData = intent.getParcelableExtra(KEY_INTENT_LOGIN_DATA)

        binding?.apply {
            goHome?.setOnClickListener {
                startActivity(intentFor<HomeActivity>(KEY_INTENT_LOGIN_DATA to userData).newTask().clearTask())
            }
        }
    }

    private fun initToolBar() {
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar)
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
//        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

}
