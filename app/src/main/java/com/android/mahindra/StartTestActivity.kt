package com.android.mahindra

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.activity_start_test.*
import kotlinx.android.synthetic.main.activity_start_test.toolbar

class StartTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_test)

        initToolBar()
        start_test.setOnClickListener(){
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)

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
