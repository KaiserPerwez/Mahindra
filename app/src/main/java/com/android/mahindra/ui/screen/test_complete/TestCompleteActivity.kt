package com.android.mahindra.ui.screen.test_complete

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivityTestCompleteBinding
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class TestCompleteActivity : AppCompatActivity() {
    private lateinit var userData: UserLoginData
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityTestCompleteBinding>(
            this,
            R.layout.activity_test_complete
        )
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
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
