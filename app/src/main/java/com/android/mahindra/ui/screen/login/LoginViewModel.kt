package com.android.mahindra.ui.screen.login

import androidx.databinding.ObservableField
import com.android.mahindra.data.remote.ApiService
import com.android.mahindra.util.extension.isDeviceOnline
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.indeterminateProgressDialog

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
        if (!activity?.isDeviceOnline()) {
            activity?.showToast("No internet connection.")
            return
        }

        val dialog = activity?.indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }

        /*disposable = apiService.getResourceSearchList(token, resourceModel)
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
                    if (result.status == Status.SUCCESS) {
                        result.data?.let {
                        }
                    }
                    else{
                        activity?.showToast(result.message)
                    }
                },
                { error ->
                    activity?.showToast(error.message ?: "Error while fetching data")
                }
            )*/
    }

    fun dispose() {
        disposable?.dispose()
    }

    fun onResume() = activity?.showToast("View model resumed")
    fun onPause() = dispose()
    fun onStop() = dispose()
}