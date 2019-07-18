package com.android.mahindra.ui.screen.register

import android.os.Environment
import androidx.databinding.ObservableField
import com.android.mahindra.data.model.api.Status
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.util.extension.isDeviceOnline
import com.iceteck.silicompressorr.SiliCompressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.selector
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File

class RegisterViewModel(private val activity: RegisterActivity) {

    var sapCode = ObservableField("")
    var firstName = ObservableField("")
    var lastName = ObservableField("")
    var email = ObservableField("")
    var mobile = ObservableField("")
    var otp = ObservableField("")
    var profilePic = ObservableField("")
    var proofPic = ObservableField("")
    var proofType = ObservableField("")


    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    fun setData() {
        activity.loginData.let {
            sapCode.set(it?.sapCode)
            email.set(it?.emailId)
            firstName.set(it?.firstName)
            lastName.set(it?.lastName)
        }
    }

    fun displayProofType() {
        val list = arrayListOf<String>()
        list.add("Aadhaar Card")
        list.add("Voter Id")
        list.add("Passport")
        list.add("Driving Licence")
        list.add("Pan Card")
        activity.selector("Click to select an option", list) { dialogInterface, index ->
            proofType.set(list[index])
        }
    }

    fun validateMobile() {
        if (!activity.isDeviceOnline()) {
            activity.toast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Validating mobile...").apply {
            setCancelable(false)
        }

        disposable = apiService.validatePhone(mobile.get())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                activity.runOnUiThread { dialog.show() }
            }
            .doAfterTerminate {
                activity.runOnUiThread { dialog.dismiss() }
            }
            .subscribe(
                { result ->
                    activity.let {
                        if (result.status == Status.SUCCESS) {
                            it.toast(result.message ?: "")
                        } else {
                            it.toast(result.message ?: "")
                        }
                    }
                },
                { error ->
                    activity.toast(error.message ?: "Error while uploading data")
                }
            )
    }

    fun redirectLogin() {
        activity.apply {
            startActivity<HomeActivity>()
            finish()
        }
    }

    fun register() {
        //   activity?.hideKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.toast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Registering user...").apply {
            setCancelable(false)
        }

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("sap_code", sapCode.get())
        builder.addFormDataPart("ph_no", mobile.get())
        builder.addFormDataPart("first_name", firstName.get())
        builder.addFormDataPart("last_name", lastName.get())
        builder.addFormDataPart("email", email.get())
        builder.addFormDataPart("id_proof_type", proofType.get())
        builder.addFormDataPart("otp", otp.get())

        val picFromPicturesDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

        if (picFromPicturesDirectory == null) {
            activity.toast("Your pic not accessible")
            return
        }
        /*val compressedprofilePic = Compressor(activity)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
            .setQuality(40)
            .setCompressFormat(Bitmap.CompressFormat.PNG)
            .setDestinationDirectoryPath(
                picFromPicturesDirectory
            )
            .compressToFile(profilePicFile)*/

        val compressedprofilePic =
            SiliCompressor.with(activity).compress(profilePic.get(), File(picFromPicturesDirectory), true)

        val profilePicFile = File(compressedprofilePic)

        builder.addFormDataPart(
            "profile_pic",
            profilePicFile.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), profilePicFile)
        )

        /*val proofPicFile = File(proofPic.get())
        val compressedproofPic = Compressor(activity)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
            .setQuality(40)
            .setCompressFormat(Bitmap.CompressFormat.PNG)
            .setDestinationDirectoryPath(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ).absolutePath
            )
            .compressToFile(proofPicFile)*/

        val compressedproofPic =
            SiliCompressor.with(activity).compress(proofPic.get(), File(picFromPicturesDirectory), true)

        val proofPicFile = File(compressedproofPic)

        builder.addFormDataPart(
            "id_proof",
            proofPicFile.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), proofPicFile)
        )

        val requestBody = builder.build()


        disposable = apiService.updateProfile(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                activity.runOnUiThread { dialog.show() }
            }
            .doAfterTerminate {
                activity.runOnUiThread { dialog.dismiss() }
            }
            .subscribe(
                { result ->
                    activity.let {
                        if (result.status == Status.SUCCESS) {

                            var resultData: UserLoginData? = activity.loginData
                            resultData?.profilePic = result.profilePic

                            it.startActivity<HomeActivity>("result" to resultData)
                        } else {
                            it.toast(result.message ?: "")
                        }
                    }
                },
                { error ->
                    activity.toast(error.message ?: "Error while uploading data")
                }
            )
    }

    fun dispose() {
        disposable?.dispose()
    }

    //    fun onResume() = activity?.toast("View model resumed")
    fun onPause() = dispose()

    fun onStop() = dispose()
}