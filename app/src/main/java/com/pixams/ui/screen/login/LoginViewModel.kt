package com.pixams.ui.screen.login

import androidx.databinding.ObservableField
import com.pixams.data.model.api.Status
import com.pixams.data.remote.api.ApiService
import com.pixams.ui.screen.home.HomeActivity
import com.pixams.util.KEY_INTENT_LOGIN_DATA
import com.pixams.util.extension.dismissKeyboard
import com.pixams.util.extension.isDeviceOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity

class LoginViewModel(private val activity: LoginActivity) {
    var sapCode = ObservableField("")
    var pin = ObservableField("")

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    fun setData() {
        activity.loginData.let {
            sapCode.set(it?.sapCode)
        }
    }

    fun forgotPin() {
        activity?.dismissKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Sending pin...").apply {
            setCancelable(false)
        }

        disposable = apiService.forgotPin(
            sapCode.get() ?: ""
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { activity.runOnUiThread { dialog.show() } }
            .doAfterTerminate { activity.runOnUiThread { dialog.dismiss() } }
            .subscribe(
                { result ->
                    activity.apply {
                        if (result.status == Status.SUCCESS) {
                            activity.apply {
                                alert(
                                    "A temporary pin has been send to your mail ID & mobile number. " +
                                            "Use it to login and reset your pin."
                                ) {
                                    title = "Link Sent"
                                    positiveButton("OK") {

                                    }
                                }.show()
                            }
                        } else
                            showToast(result.message ?: "")
                    }
                },
                { error ->
                    activity.showToast(error.message ?: "Error while fetching data")
                }
            )
    }

    fun loginUser() {
        activity?.dismissKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }

        disposable = apiService.userLogin(
            sapCode.get() ?: "",
            pin.get() ?: ""
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { activity.runOnUiThread { dialog.show() } }
            .doAfterTerminate { activity.runOnUiThread { dialog.dismiss() } }
            .subscribe(
                { result ->
                    activity.apply {
                        if (result.status == Status.SUCCESS) {
                            startActivity<HomeActivity>(KEY_INTENT_LOGIN_DATA to result)
                            finish()
                        } else
                            showToast(result.message ?: "")
                    }
                },
                { error ->
                    activity.showToast(error.message ?: "Error while fetching data")
                }
            )
    }

    private fun dispose() {
        disposable?.dispose()
    }

    fun onPause() = dispose()

}