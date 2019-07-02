package com.android.mahindra.ui.screen.register

import androidx.databinding.ObservableField
import com.android.mahindra.data.model.api.Status
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.util.extension.isDeviceOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_question.*
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

    fun register() {
        //   activity?.hideKeyboard()
        if (!activity?.isDeviceOnline()) {
            activity?.toast("No internet connection.")
            return
        }

        val dialog = activity?.indeterminateProgressDialog("Registering user...").apply {
            setCancelable(false)
        }

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val profilePicFile = File(profilePic.get())
        val proofPicFile = File(proofPic.get())

        builder.addFormDataPart("sap_code", sapCode.get())
        builder.addFormDataPart("ph_no", mobile.get())
        builder.addFormDataPart("first_name", firstName.get())
        builder.addFormDataPart("last_name", lastName.get())
        builder.addFormDataPart("email", email.get())
        builder.addFormDataPart("id_proof_type", proofType.get())
        builder.addFormDataPart("otp", otp.get())

        builder.addFormDataPart(
            "profile_pic",
            profilePicFile.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), profilePicFile)
        )
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
                    activity?.let {
                        if (result.status == Status.SUCCESS) {
                                it.startActivity<HomeActivity>("result" to result)
                        } else {
                            it.toast(result.message ?: "")
                        }
                    }
                },
                { error ->
                    activity?.toast(error.message ?: "Error while uploading data")
                }
            )
    }

    fun dispose() {
        disposable?.dispose()
    }

    fun onResume() = activity?.toast("View model resumed")
    fun onPause() = dispose()
    fun onStop() = dispose()
}