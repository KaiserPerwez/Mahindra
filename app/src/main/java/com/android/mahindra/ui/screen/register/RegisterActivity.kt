package com.android.mahindra.ui.screen.register

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivityRegisterBinding
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import com.android.mahindra.util.extension.dismissKeyboard
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mukesh.OnOtpCompletionListener
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var loginData: UserLoginData? = null

    private val REQUEST_CAPTURE_IMAGE = 100

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
    }
    private val viewModel by lazy {
        RegisterViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        supportActionBar?.title = "Register"
        loginData = intent.getParcelableExtra(KEY_INTENT_LOGIN_DATA)
        binding.apply {
            vm = viewModel
            vm?.setData()
            act = this@RegisterActivity

            register.setOnClickListener {
                startActivity<HomeActivity>()
            }

            /*mobile.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    if (mobile.text?.length == 10) {
                        vm?.validateMobile()
                    } else {
                        mobile.error = "Enter a valid number."
                    }
                }
            }*/

            pin_otpview.setPinViewEventListener { pinview, fromUser ->
                if(!pinview.value.isNullOrEmpty())
                    vm?.userPin?.set(pinview.value?:"")
                else
                    toast("Please enter pin.")
            }

            /*repin_otpview.setOtpCompletionListener {
                if(it.isNullOrEmpty() && it == vm?.userPin?.get())
                    vm?.reUserPin?.set(it?:"")
                else
                    toast("Please re enter correct pin.")
            }*/

            repin_otpview.setPinViewEventListener { pinview, fromUser ->
                if(!pinview.value.isNullOrEmpty() && pinview.value == vm?.userPin?.get())
                    vm?.reUserPin?.set(pinview.value?:"")
                else
                    toast("Please re enter correct pin.")
            }
        }

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
//                imageView.setImageURI(Uri.parse(imageFilePath));
            } else if (resultCode == RESULT_CANCELED) {
                binding?.vm?.profilePic?.set("")
                toast("You cancelled the operation")
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(view: View): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            if (view.id == R.id.upload_profile_photo)
                binding?.vm?.profilePic?.set(absolutePath)
            else
                binding?.vm?.proofPic?.set(absolutePath)
        }
    }

    fun initPermission(view: View) {

        if (view.id == R.id.id_proof_layout && binding.vm?.proofType?.get() == "") {
            toast("Please select ID Proof.")
            return
        }

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // do you work now
                        openCameraIntent(view)
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
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

    private fun openCameraIntent(view: View) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(view)
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "$packageName.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CAPTURE_IMAGE)
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

}
