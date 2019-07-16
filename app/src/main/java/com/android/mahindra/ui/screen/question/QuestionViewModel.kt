package com.android.mahindra.ui.screen.question

import androidx.databinding.ObservableField
import com.android.mahindra.data.model.api.AnswerModel
import com.android.mahindra.data.model.api.Question
import com.android.mahindra.data.model.api.Status
import com.android.mahindra.data.model.api.SubmitAnswerModel
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.ui.screen.home.HomeActivity
import com.android.mahindra.util.extension.isDeviceOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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

    fun submitData(testId: String, testName: String) {
        //   activity?.hideKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Submitting your answers.Please be patient..").apply {
            setCancelable(false)
        }

        val list = mutableListOf<AnswerModel>()
        questionList?.forEach {
            val answerModel = AnswerModel(it.questionId, it.type?.toLowerCase(), it.answer)
            list.add(answerModel)
        }

        val answerRequestModel = SubmitAnswerModel(
            testId,
            activity.userData.sapCode,
            SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()),
            list
        )

        disposable = apiService.submitAnswers(answerRequestModel)
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
                            alert("Thanks for the exam") {
                                okButton {
                                        startActivity(intentFor<HomeActivity>("result" to userData).newTask().clearTask())
                                }
                            }
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

    fun uploadImage(imagePath: String) {
        if (!activity.isDeviceOnline()) {
            return
        }

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("test_id", activity.item.testId.toString() ?: "")
        builder.addFormDataPart("test_name", activity.item.testName)
        builder.addFormDataPart("sap_code", activity.userData.sapCode)

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
                //                activity.runOnUiThread { dialog.show() }
            }
            .doAfterTerminate {
                //                activity.runOnUiThread { dialog.dismiss() }
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