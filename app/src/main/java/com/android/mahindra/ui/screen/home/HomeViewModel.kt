package com.android.mahindra.ui.screen.home

import androidx.databinding.ObservableField
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.util.extension.dismissKeyboard
import com.android.mahindra.util.extension.isDeviceOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.indeterminateProgressDialog

class HomeViewModel(private val activity: HomeActivity) {

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }
    val list = ObservableField(listOf<ExamsModel>())
    fun fetchExams(sapCode: String) {
        activity?.dismissKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }


        disposable = apiService.getExams(sapCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                activity?.runOnUiThread { dialog.show() }
            }
            .doAfterTerminate {
                activity?.runOnUiThread { dialog.dismiss() }
            }
            .subscribe(
                { result ->
                    result?.data?.let {
                        if (it.isNotEmpty()) {
                            activity?.setUpViewPager(it)
                        }
                    }
                },
                { error ->
                    activity?.showToast(error.message ?: "Error while fetching data")
                }
            )
    }

    private fun dispose() {
        disposable?.dispose()
    }

    fun onPause() = dispose()
}