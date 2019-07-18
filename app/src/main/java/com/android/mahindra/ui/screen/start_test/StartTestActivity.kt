package com.android.mahindra.ui.screen.start_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.ui.screen.question.QuestionActivity
import kotlinx.android.synthetic.main.activity_start_test.*

class StartTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_test)

        val item=intent.getParcelableExtra<ExamsModel>("item")
        val userData=intent.getParcelableExtra<UserLoginData>("user_data")

        test_duration.text = "1. Test duration ${item.testDuration} min"

        initToolBar()
        start_test.setOnClickListener {
            item?.let {
                val intentNext = Intent(this, QuestionActivity::class.java).apply {
                    putExtra("item", item)
                    putExtra("user_data", userData)
                }
                startActivity(intentNext)
            }

        }
    }

    private fun initToolBar() {
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener(
            View.OnClickListener { onBackPressed() })
    }

}
