package com.android.mahindra.ui.screen.register

import android.app.Dialog
import android.os.Environment
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.*
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.ui.screen.review.ReviewAdapter
import com.android.mahindra.ui.screen.validate.ValidateActivity
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import com.android.mahindra.util.extension.dismissKeyboard
import com.android.mahindra.util.extension.isDeviceOnline
import com.iceteck.silicompressorr.SiliCompressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.*
import java.io.File

class RegisterViewModel(private val activity: RegisterActivity) {

    var sapCode = ObservableField("")
    var name = ObservableField("")
//    var lastName = ObservableField("")
    var email = ObservableField("")
    var mobile = ObservableField("")
    var otp = ObservableField("")
    var profilePic = ObservableField("")
    var proofPic = ObservableField("")
    var userPin = ObservableField("")
    var reUserPin = ObservableField("")
    var proofType = ObservableField("")


    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }


    fun setData() {
        activity.loginData.let {
            sapCode.set(it?.sapCode)
            email.set(it?.emailId)
            name.set(it?.emp_name)
            mobile.set(it?.mobile)
//            lastName.set(it?.lastName)
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
            startActivity<ValidateActivity>()
            finish()
        }
    }

    fun contactAdmin(){
        if (!activity.isDeviceOnline()) {
            activity.toast("No internet connection.")
            return
        }

        disposable = apiService.getChangeRequests()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    activity.let {
                        if (!result.optionList.isNullOrEmpty()) {
                            it.showReviewDialog(result.optionList)
                        }/* else {
                            it.toast(result.message ?: "")
                        }*/
                    }
                },
                { error ->
                    activity.toast(error.message ?: "Error while retrieving data")
                }
            )
    }

    fun submitContactForm(listOption: List<Option>){
        if (!activity.isDeviceOnline()) {
            activity.toast("No internet connection.")
            return
        }
        val queryList = ContactAdminRequest(listOption)
        disposable = apiService.contactAdmin(queryList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    activity.let {
                        if (result.status == true) {
                            it.toast(result.message ?: "")
                        } else {
                            it.toast(result.message ?: "")
                        }
                    }
                },
                { error ->
                    activity.toast(error.message ?: "Error while retrieving data")
                }
            )
    }



    fun register() {
        activity?.dismissKeyboard()

        activity?.alert("I agree on the authenticity of the information" +
                "submitted above. Any act of misinformation may lead to termination, based" +
                "upon the discretion of Mahindra Finance.") {
            title = "Terms & Conditions"
            positiveButton("Accept") {
                registerUser()
            }
            negativeButton("Decline") {

            }
        }.show()

    }

    private fun registerUser(){
        if (!activity.isDeviceOnline()) {
            activity.toast("No internet connection.")
            return
        }

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("sap_code", sapCode.get() ?: "")
        builder.addFormDataPart("ph_no", mobile.get() ?: "")
        builder.addFormDataPart("emp_name", name.get() ?: "")
//        builder.addFormDataPart("last_name", lastName.get() ?: "")
        builder.addFormDataPart("email", email.get() ?: "")
        builder.addFormDataPart("id_proof_type", proofType.get() ?: "")
        builder.addFormDataPart("otp", otp.get() ?: "")
        builder.addFormDataPart("user_pin", reUserPin.get() ?: "")

        val picFromPicturesDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

        if (picFromPicturesDirectory == null) {
            activity.toast("Your pic not accessible")
            return
        }
        if (profilePic?.get()?.isBlank() == true) {
            activity.toast("Uploading your pic is mandatory")
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

        val compressedProfilePic =
            SiliCompressor.with(activity).compress(profilePic.get(), File(picFromPicturesDirectory))

        val profilePicFile = File(profilePic.get())

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

        if (proofPic?.get()?.isBlank() == true) {
            activity.toast("Uploading your proof pic is mandatory")
            return
        }

        val compressedProofPic =
            SiliCompressor.with(activity).compress(proofPic.get(), File(picFromPicturesDirectory), false)

        val proofPicFile = File(proofPic.get())

        builder.addFormDataPart(
            "id_proof",
            proofPicFile.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), proofPicFile)
        )

        val requestBody = builder.build()

        val dialog = activity.indeterminateProgressDialog("Registering user...").apply {
            setCancelable(false)
        }

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

                            it.startActivity<HomeActivity>(KEY_INTENT_LOGIN_DATA to resultData)
                            it.finish()
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

    private fun dispose() {
        disposable?.dispose()
    }

    fun onPause() = dispose()

}