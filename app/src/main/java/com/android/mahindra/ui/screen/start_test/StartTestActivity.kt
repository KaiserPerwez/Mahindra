package com.android.mahindra.ui.screen.start_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.ui.screen.question.QuestionActivity
import kotlinx.android.synthetic.main.activity_start_test.*
import kotlinx.android.synthetic.main.activity_start_test.toolbar

class StartTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_test)

        initToolBar()
        start_test.setOnClickListener(){
            val item=intent.getParcelableExtra<ExamsModel>("item")
            item?.let {
                val intentNext = Intent(this, QuestionActivity::class.java).apply {
                    putExtra("item", item)
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
