package com.android.mahindra.ui.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivitySettingsBinding
import com.android.mahindra.ui.screen.resetPin.ResetActivity
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import org.jetbrains.anko.startActivity

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivitySettingsBinding>(this, R.layout.activity_settings)
    }
    val loginData by lazy {
        intent.getParcelableExtra(KEY_INTENT_LOGIN_DATA) as UserLoginData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        initToolBar()
        binding?.btnReset?.setOnClickListener {
            startActivity<ResetActivity>(KEY_INTENT_LOGIN_DATA to loginData)
            finish()
        }
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
