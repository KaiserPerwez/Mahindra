package com.android.mahindra.ui.screen.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.databinding.ActivityLoginBinding
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }
    private val viewModel by lazy {
        LoginViewModel(this)
    }

    //methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        binding.vm = viewModel

        supportActionBar?.title = "Login"
        /*login.setOnClickListener{
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }*/
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
    }

}
