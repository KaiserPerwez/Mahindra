package com.android.mahindra.ui.common


/**
 * @author Kaiser Perwez
 */
 
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class CaptureCameraUtils(val activity: Activity) {

    fun captureImage() {
        checkPermissions()
    }

    private fun checkPermissions() {
        Dexter.withActivity(activity)
            .withPermission(
                android.Manifest.permission.CAMERA
            )
            .withListener(object : PermissionListener {
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    dispatchTakePictureIntent()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    showCameraPermissionDetailsDialog()
                }
            }).withErrorListener { activity.toast("Error occurred while asking for camera permission!") }
            .onSameThread()
            .check()
    }

    val REQUEST_IMAGE_CAPTURE = 1
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE, null)
            }
        }

    }


    private fun showCameraPermissionDetailsDialog() {
        activity.alert("This app needs camera permission to capture images", "Need Permissions") {
            positiveButton("Allow") {
                it.dismiss()
                checkPermissions()
            }
            negativeButton("Ignore") {
                it.dismiss()
                // listener.PermissionDenied()
            }
        }.show()
    }
}
