package com.android.mahindra.ui.screen.review

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.android.mahindra.R

import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setSupportActionBar(toolbar)
    }

}
