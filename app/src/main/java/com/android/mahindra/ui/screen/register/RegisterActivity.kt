package com.android.mahindra.ui.screen.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivityRegisterBinding
import com.android.mahindra.ui.screen.home.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var loginData: UserLoginData

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
    }
    private val viewModel by lazy {
        RegisterViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUiAndListeners()

        register.setOnClickListener() {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initUiAndListeners() {

        loginData = intent.getParcelableExtra("result")

        binding.vm = viewModel

        supportActionBar?.title = "Register"
    }
}
