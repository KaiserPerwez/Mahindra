package com.pixams.ui.screen.resetPin

import androidx.databinding.ObservableField
import com.pixams.data.model.api.Status
import com.pixams.data.remote.api.ApiService
import com.pixams.util.extension.dismissKeyboard
import com.pixams.util.extension.isDeviceOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast

class ResetViewModel(private val activity: ResetActivity) {
    var sapCode = ObservableField("")
    var oldPin = ObservableField("")
    var newPin = ObservableField("")
    var confirmPin = ObservableField("")

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    fun changePin() {
        activity?.dismissKeyboard()

        if (oldPin.get().equals("")) {
            activity.showToast("Please enter old PIN.")
            return
        }

        if (confirmPin.get().equals("")) {
            activity.showToast("Please enter new PIN.")
            return
        }

        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Sending data...").apply {
            setCancelable(false)
        }

        disposable = apiService.changePin(
            sapCode.get() ?: "",
            oldPin.get() ?: "",
            confirmPin.get() ?: ""
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { activity.runOnUiThread { dialog.show() } }
            .doAfterTerminate { activity.runOnUiThread { dialog.dismiss() } }
            .subscribe(
                { result ->
                    activity.apply {
                        if (result.status == Status.SUCCESS) {
                            toast(result.message ?: "")
                            finish()
                        } else
                            toast(result.message ?: "")
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