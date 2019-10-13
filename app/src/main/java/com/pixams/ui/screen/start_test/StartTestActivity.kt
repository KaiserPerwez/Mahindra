package com.pixams.ui.screen.start_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pixams.R
import com.pixams.data.model.api.ExamsModel
import com.pixams.data.model.api.Status
import com.pixams.data.model.api.UserLoginData
import com.pixams.data.remote.api.ApiService
import com.pixams.databinding.ActivityStartTestBinding
import com.pixams.ui.screen.question.QuestionActivity
import com.pixams.util.KEY_INTENT_EXAM_MODEL
import com.pixams.util.KEY_INTENT_LOGIN_DATA
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class StartTestActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    private lateinit var item: ExamsModel
    private lateinit var userData: UserLoginData
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityStartTestBinding>(this, R.layout.activity_start_test)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        initToolBar()
        item = intent.getParcelableExtra<ExamsModel>(KEY_INTENT_EXAM_MODEL)
        userData = intent.getParcelableExtra<UserLoginData>(KEY_INTENT_LOGIN_DATA)

        binding?.apply {

            testDuration?.text = "1. Test duration ${item.testDuration} min"

            startTest?.setOnClickListener {

                alert(
                    "Your camera will be used to click random multiple photographs during the " +
                            "assessment for the purpose of authenticity check."
                ) {
                    positiveButton("Allow Camera") {
                        startTest()
                    }
                    negativeButton("Decline") { }
                }.show()
            }
        }
    }

    private fun startTest() {
        val dialog = indeterminateProgressDialog("Loding data...").apply {
            setCancelable(false)
        }

        disposable = apiService.changeStatus(
            userData.sapCode ?: "",
            item.testId?.toString() ?: "",
            item.scheduledId?.toString() ?: ""
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { runOnUiThread { dialog.show() } }
            .doAfterTerminate { runOnUiThread { dialog.dismiss() } }
            .subscribe(
                { result ->
                    if (result.status == Status.SUCCESS) {
                        startActivity<QuestionActivity>(KEY_INTENT_EXAM_MODEL to item, KEY_INTENT_LOGIN_DATA to userData)
                        finish()
                    } else
                        toast(result.message ?: "")
                },
                { error ->
                    toast(error.message ?: "Error while fetching data")
                }
            )

    }

    private fun initToolBar() {
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    fun changeStatus() {

    }

    private fun dispose() {
        disposable?.dispose()
    }

    override fun onPause(){
        super.onPause()
        dispose()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
