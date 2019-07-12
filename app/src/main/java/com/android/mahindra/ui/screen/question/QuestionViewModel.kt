package com.android.mahindra.ui.screen.question

import android.graphics.Bitmap
import android.os.Environment
import androidx.databinding.ObservableField
import com.android.mahindra.data.model.api.AnswerModel
import com.android.mahindra.data.model.api.Question
import com.android.mahindra.data.model.api.Status
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.util.extension.isDeviceOnline
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File

class QuestionViewModel(private val activity: QuestionActivity) {

    //header
    var indexCurrentQuestion = ObservableField("0")
    var totalQuestions = ObservableField("0")
    var timeToExpire = ObservableField("")

    var currentQuestion = ObservableField<Question>()
    var questionList = mutableListOf<Question>()


    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    /*   private val userPrefs by lazy {
           PreferenceHelper.customPrefs(searchActivity, PreferenceHelper.USER_PREF)
       }
     */
    fun fetchData(testId: String) {
        //   activity?.hideKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }

        disposable = apiService.getQuestions(testId)
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
                        /*  if (result.status == Status.SUCCESS) {
                                              if (result.isFirstLogin == true) {
                                                  it.startActivity<RegisterActivity>("result" to result)
                                              } else {
                                                  it.startActivity<HomeActivity>("result" to result)
                                              }
                                          } else {
                                              it.showToast(result.message ?: "")
                                          }*/
                        result?.questions?.let {
                            if (it.isNotEmpty()) {
                                indexCurrentQuestion.set("1")
                                totalQuestions.set(it.size.toString())
                                questionList.addAll(it)

                                currentQuestion.set(it.get(0))
                                activity.initViewPager()
                            }
                        }
                    }
                },
                { error ->
                    activity.showToast(error.message ?: "Error while fetching data")
                }
            )
    }

    fun uploadImage(imagePath: String) {
        //   activity?.hideKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("test_id", activity.item.testId.toString() ?: "")
        builder.addFormDataPart("test_name", activity.item.testName)
        builder.addFormDataPart("sap_code", "23066056")

        val imageCapture = File(imagePath)

        builder.addFormDataPart(
            "capture_image",
            imageCapture.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), imageCapture)
        )
        val requestBody = builder.build()

        disposable = apiService.saveCaptureImage(requestBody)
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

                    }
                },
                { error ->

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