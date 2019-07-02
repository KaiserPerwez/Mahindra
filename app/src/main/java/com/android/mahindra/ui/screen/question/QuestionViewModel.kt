package com.android.mahindra.ui.screen.question

import androidx.databinding.ObservableField
import com.android.mahindra.data.model.api.AnswerModel
import com.android.mahindra.data.model.api.Question
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.util.extension.isDeviceOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.indeterminateProgressDialog

class QuestionViewModel(private val activity: QuestionActivity) {

    //header
    var indexCurrentQuestion = ObservableField("0")
    var totalQuestions = ObservableField("0")
    var timeToExpire = ObservableField("")

    var currentQuestion = ObservableField<Question>()
    var questionList = mutableListOf<Question>()

    var answerList = mutableListOf<AnswerModel>()

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    /*   private val userPrefs by lazy {
           PreferenceHelper.customPrefs(searchActivity, PreferenceHelper.USER_PREF)
       }
     */
    fun fetchData(testId: String) {
        //   activity?.hideKeyboard()
        if (!activity?.isDeviceOnline()) {
            activity?.showToast("No internet connection.")
            return
        }

        val dialog = activity?.indeterminateProgressDialog("Loading data...").apply {
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
                    activity?.let {
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
                                indexCurrentQuestion?.set("1")
                                totalQuestions?.set(it.size.toString())
                                questionList?.addAll(it)
                                it?.forEach {
                                    answerList?.add(AnswerModel(it.questionId ?: "0", it.type ?: "", ""))
                                }
                                currentQuestion?.set(it.get(0))
                            }
                        }
                    }
                },
                { error ->
                    activity?.showToast(error.message ?: "Error while fetching data")
                }
            )
    }

    fun dispose() {
        disposable?.dispose()
    }

    fun onResume() = activity?.showToast("View model resumed")
    fun onPause() = dispose()
    fun onStop() = dispose()
}