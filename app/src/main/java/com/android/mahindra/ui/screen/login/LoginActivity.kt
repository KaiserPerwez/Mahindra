package com.android.mahindra.ui.screen.login

import android.Manifest
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivityLoginBinding
import com.android.mahindra.ui.screen.register.RegisterViewModel
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import kotlin.math.roundToInt

class LoginActivity : AppCompatActivity() {

    var loginData: UserLoginData? = null

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    private val viewModel by lazy {
        LoginViewModel(this)
    }

    //methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        windowManager.defaultDisplay.getMetrics(DisplayMetrics())

        initUiAndListeners()
        initPermissionToCaptureImageInBackground()
    }

    private fun initPermissionToCaptureImageInBackground() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        //TODO: permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    private fun initUiAndListeners() {

        loginData = intent.getParcelableExtra(KEY_INTENT_LOGIN_DATA)
        binding.apply {
            vm = viewModel
            vm?.setData()
        }

        sap_layout.layoutParams.width = (4 * 70 * resources.displayMetrics.density).roundToInt()
        pin_otpview.setPinViewEventListener { pinview, fromUser ->
            if(!pinview.value.isNullOrEmpty())
                binding?.vm?.pin?.set(pinview.value?:"")
            else
                toast("Please enter pin.")
        }

    }

    fun showToast(msg: String) {
        toast(msg)
    }

    override fun onPause() {
        super.onPause()
        binding?.vm?.onPause()
    }


}
