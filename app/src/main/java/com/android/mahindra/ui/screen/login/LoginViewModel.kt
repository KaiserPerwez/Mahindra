package com.android.mahindra.ui.screen.login

import androidx.databinding.ObservableField
import com.android.mahindra.data.model.api.Status
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.ui.screen.register.RegisterActivity
import com.android.mahindra.util.extension.isDeviceOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity

class LoginViewModel(private val activity: LoginActivity) {
    var sapCode = ObservableField("")

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    /*   private val userPrefs by lazy {
           PreferenceHelper.customPrefs(searchActivity, PreferenceHelper.USER_PREF)
       }
     */
    fun fetchData() {
        //   activity?.hideKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }

        disposable = apiService.userLogin(sapCode.get() ?: "")
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
                    activity.apply {
                        if (result.status == Status.SUCCESS) {
                            if (result.isFirstLogin == true) {
                                startActivity<RegisterActivity>("result" to result)
                            } else {
                                startActivity<HomeActivity>("result" to result)
                            }
                            finish()
                        } else {
                            showToast(result.message ?: "")
                        }
                    }
                },
                { error ->
                    activity.showToast(error.message ?: "Error while fetching data")
                }
            )
    }

    fun dispose() {
        disposable?.dispose()
    }

    fun onResume() = activity.showToast("View model resumed")
    fun onPause() = dispose()
    fun onStop() = dispose()
}