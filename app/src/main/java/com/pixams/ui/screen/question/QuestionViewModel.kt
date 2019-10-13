package com.pixams.ui.screen.question

import android.os.Environment
import androidx.databinding.ObservableField
import com.pixams.data.model.api.*
import com.pixams.data.remote.api.ApiService
import com.pixams.ui.screen.test_complete.TestCompleteActivity
import com.pixams.util.KEY_INTENT_LOGIN_DATA
import com.pixams.util.extension.isDeviceOnline
import com.iceteck.silicompressorr.SiliCompressor
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
    var timeToExpire = ObservableField("")

    var currentQuestion = ObservableField<Question>()
    var questionList = ObservableField(listOf<Question>())


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
                                questionList.set(it)

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

    fun submitData(testDetail: ExamsModel) {
        //   activity?.hideKeyboard()
        if (!activity.isDeviceOnline()) {
            activity.showToast("No internet connection.")
            return
        }

        val dialog = activity.indeterminateProgressDialog("Submitting your answers.Please be patient..").apply {
            setCancelable(false)
        }

        val list = mutableListOf<AnswerModel>()

        questionList?.get()?.forEach {
            val answerModel = AnswerModel(it.questionId, it.type?.toLowerCase(), it.answer)
            list.add(answerModel)
        }

        val answerRequestModel = SubmitAnswerModel(
            testDetail.testId?.toString() ?: "" ,
            testDetail.scheduledId?.toString() ?: "" ,
            activity.userData.sapCode,
            SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()),
            list as List<AnswerModel>
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
                                    startActivity(intentFor<TestCompleteActivity>(KEY_INTENT_LOGIN_DATA to userData).newTask().clearTask())
                                }
                            }.show()
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

        builder.addFormDataPart("test_id", activity.item.testId.toString())
        builder.addFormDataPart("scheduled_id", activity.item.scheduledId.toString())
        builder.addFormDataPart("test_name", activity.item.testName)
        builder.addFormDataPart("sap_code", activity.userData.sapCode)

        val picFromPicturesDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

        val compressedProfilePic =
            SiliCompressor.with(activity).compress(imagePath, File(picFromPicturesDirectory))

        val imageCapture = File(compressedProfilePic)

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

    fun toggleReview() {
        currentQuestion.get()?.let { quesn ->
            quesn.statusReview = if (quesn.statusReview == "0") "1" else "0"
            activity?.binding?.toggleReview?.text =
                if (quesn.statusReview == "0")
                    "Mark as Review"
                else
                    "Marked as Review"
            currentQuestion?.set(quesn)

            questionList?.get()?.let {
                val tempList = it
                questionList?.set(tempList?.apply {
                    filter { it.questionId == quesn.questionId }?.get(0)?.statusReview = quesn.statusReview
                })
            }
            activity?.binding?.txtReviewCounter?.text =
                "Review: ${questionList?.get()?.filter { it.statusReview == "1" }?.size ?: 0}"
        }
    }

    fun dispose() {
        disposable?.dispose()
    }

    fun onResume() = activity.showToast("View model resumed")
    fun onPause() = dispose()
    fun onStop() = dispose()
}