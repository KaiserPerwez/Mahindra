package com.pixams.ui.screen.validate

import androidx.databinding.ObservableField
import com.pixams.data.model.api.Status
import com.pixams.data.remote.api.ApiService
import com.pixams.ui.screen.login.LoginActivity
import com.pixams.ui.screen.register.RegisterActivity
import com.pixams.util.KEY_INTENT_LOGIN_DATA
import com.pixams.util.extension.dismissKeyboard
import com.pixams.util.extension.isDeviceOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity

class ValidateViewModel(private val activity: ValidateActivity) {
    var sapCode = ObservableField("")

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }


    fun loginUser() {
        activity.dismissKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }

        disposable = apiService.userValidate(sapCode.get() ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { activity.runOnUiThread { dialog.show() } }
            .doAfterTerminate { activity.runOnUiThread { dialog.dismiss() } }
            .subscribe(
                { result ->
                    activity.apply {
                        if (result.status == Status.SUCCESS) {
                            if (result.isFirstLogin == true)
                                startActivity<RegisterActivity>(KEY_INTENT_LOGIN_DATA to result)
                            else
                                startActivity<LoginActivity>(KEY_INTENT_LOGIN_DATA to result)
//                                    startActivity<HomeActivity>(KEY_INTENT_LOGIN_DATA to result)
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