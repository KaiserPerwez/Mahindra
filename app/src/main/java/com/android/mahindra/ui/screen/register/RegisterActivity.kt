package com.android.mahindra.ui.screen.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.mahindra.R
import com.android.mahindra.ui.screen.home.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        register.setOnClickListener() {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
