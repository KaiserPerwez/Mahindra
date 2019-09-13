package com.android.mahindra.ui.screen.resetPin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivityResetPinBinding
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import kotlinx.android.synthetic.main.activity_reset_pin.*
import org.jetbrains.anko.toast

class ResetActivity : AppCompatActivity() {

    var loginData: UserLoginData? = null

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityResetPinBinding>(this, R.layout.activity_reset_pin)
    }

    //methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUiAndListeners()
    }

    private fun initUiAndListeners() {

        loginData = intent.getParcelableExtra(KEY_INTENT_LOGIN_DATA)
        binding.apply {
            vm = ResetViewModel(this@ResetActivity)
            vm?.sapCode?.set(loginData?.sapCode)
            pin_newotpview?.setPinViewEventListener { pinview, fromUser ->
                if (!pinview.value.isNullOrEmpty())
                    vm?.newPin?.set(pinview.value ?: "")
                else
                    toast("Please enter pin.")
            }

            pin_confirmotpview.setPinViewEventListener { pinview, fromUser ->
                if (!pinview.value.isNullOrEmpty() && pinview.value == vm?.newPin?.get())
                    vm?.confirmPin?.set(pinview.value ?: "")
                else
                    toast("Please re enter correct pin.")
            }
        }

    }

    private fun initToolBar() {
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        /*    setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    fun showToast(msg: String) {
        toast(msg)
    }

    override fun onPause() {
        super.onPause()
        binding?.vm?.onPause()
    }

}
