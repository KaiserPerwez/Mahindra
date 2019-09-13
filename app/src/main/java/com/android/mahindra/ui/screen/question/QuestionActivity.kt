package com.android.mahindra.ui.screen.question

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.databinding.ActivityQuestionBinding
import com.android.mahindra.ui.screen.review.ReviewAdapter
import com.android.mahindra.util.KEY_INTENT_EXAM_MODEL
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import com.androidhiddencamera.CameraConfig
import com.androidhiddencamera.CameraError
import com.androidhiddencamera.HiddenCameraActivity
import com.androidhiddencamera.HiddenCameraUtils
import com.androidhiddencamera.config.CameraFacing
import com.androidhiddencamera.config.CameraImageFormat
import com.androidhiddencamera.config.CameraResolution
import com.androidhiddencamera.config.CameraRotation
import kotlinx.android.synthetic.main.activity_question.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.io.File


class QuestionActivity : HiddenCameraActivity() {

    private val REQ_CODE_CAMERA_PERMISSION = 1253

    private var mCameraConfig: CameraConfig? = null

    lateinit var item: ExamsModel
    lateinit var userData: UserLoginData

    val binding by lazy {
        DataBindingUtil.setContentView<ActivityQuestionBinding>(this, R.layout.activity_question)
    }
    private val viewModel by lazy {
        QuestionViewModel(this)
    }
    var countDownTimer: CountDownTimer? = null
    var countDownTimerRandom: CountDownTimer? = null

    val updateHandler = Handler()
    lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        item = intent.getParcelableExtra(KEY_INTENT_EXAM_MODEL)

        userData = intent.getParcelableExtra(KEY_INTENT_LOGIN_DATA)
        item.let {
            val testId = it.testId.toString()
            initUiAndListeners(it.testDuration ?: "0")
            binding?.vm?.fetchData(testId)
        }

        cameraConfigration()

    }

    private fun cameraConfigration() {
        //Setting camera configuration
        mCameraConfig = CameraConfig()
            .getBuilder(this)
            .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
            .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
            .setImageFormat(CameraImageFormat.FORMAT_JPEG)
            .setImageRotation(CameraRotation.ROTATION_270)
            .build()

        permissionCheck()

        ramdomImageCapture(5)
    }

    private fun permissionCheck() {
        //Check for the camera permission for the runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //Start camera preview
            startCamera(mCameraConfig)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.CAMERA),
                REQ_CODE_CAMERA_PERMISSION
            )
        }
    }

    private fun ramdomImageCapture(ran: Int) {
        val rand: Long = (ran * 1000).toLong()
        runnable = Runnable {
            //Take picture using the camera without preview.
            takePicture()

            val rnds = (20..60).random()
            ramdomImageCapture(rnds)
        }

        updateHandler.postDelayed(runnable, rand)

    }

    override fun onImageCapture(imageFile: File) {
        binding?.vm?.uploadImage(imageFile.absolutePath)
    }

    private fun initUiAndListeners(timeInMins: String) {
        supportActionBar?.title = "Questions"
        initToolBar()
//        initViewPager()
        binding.vm = viewModel
        val timeToExpire = timeInMins.toLong() * 60 * 1000
        countDownTimer = object : CountDownTimer(timeToExpire, 1000) {
            override fun onFinish() {
                binding?.vm?.timeToExpire?.set("TimeOut")
//                countDownTimerRandom?.cancel()
                updateHandler.removeCallbacks(runnable)
                binding?.submit?.performClick()
            }

            override fun onTick(time: Long) {
                var rem = time / 1000

                val h = rem / (60 * 60)
                rem %= (60 * 60)

                val m = rem / 60
                rem %= 60

                binding?.vm?.timeToExpire?.set(
                    "${if (h < 10) "0$h" else h}:" +
                            "${if (m < 10) "0$m" else m}:" +
                            "${if (rem < 10) "0$rem" else rem}"
                )
            }
        }
        countDownTimer?.start()

        setViewDisabled(previous)

    }


    override fun onCameraError(errorCode: Int) {
        when (errorCode) {
            CameraError.ERROR_CAMERA_OPEN_FAILED ->
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, R.string.error_cannot_open, Toast.LENGTH_LONG).show()
            CameraError.ERROR_IMAGE_WRITE_FAILED ->
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, R.string.error_cannot_write, Toast.LENGTH_LONG).show()
            CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE ->
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(this, R.string.error_cannot_get_permission, Toast.LENGTH_LONG).show()
            CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION ->
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this)
            CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA -> Toast.makeText(
                this,
                R.string.error_not_having_camera,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    /*private fun initViewPager() {
        binding?.vm?.apply {
            val quesAdapter = QuestionAdapter(questionList, supportFragmentManager)
            viewPager?.apply {
                adapter = quesAdapter
                previous?.setOnClickListener {
                    if (currentItem > 0)
                        currentItem -= 1
                }
                next?.setOnClickListener {
                    if (currentItem < questionList.size)
                        currentItem += 1
                }
            }
        }
    }*/

    fun initViewPager() {
        binding?.vm?.apply {
            questionList?.get()?.let {

                binding?.txtQuesnCounter?.text = "1 / ${it.size}"
                binding?.txtUnattemptedCounter?.text = "Unattempted: ${it.size}"
                binding?.txtAttemptedCounter?.text = "Attempted: 0"

                val quesAdapter = QuestionAdapter(it, supportFragmentManager)
                binding?.viewPager?.apply {
                    offscreenPageLimit = it.size
                    setOnTouchListener { view, motionEvent ->
                        this.currentItem = this.currentItem
                        return@setOnTouchListener true
                    }
                    adapter = quesAdapter
                    binding?.ivQuesnBox?.setOnClickListener {
                        saveAnswer(currentItem, quesAdapter)
                        showReviewDialog()
                    }
                    binding?.previous?.setOnClickListener { v ->
                        if (currentItem > 0) {
                            saveAnswer(currentItem, quesAdapter)
                            currentItem -= 1
                            setQuestionOnUi(currentItem)
                        }
                        setViewEnabled(next)
                        binding?.submit?.visibility = View.GONE
                    }
                    binding?.next?.setOnClickListener { v ->
                        if (currentItem < it.size) {
                            saveAnswer(currentItem, quesAdapter)
                            currentItem += 1
                            setQuestionOnUi(currentItem)
                        }

                        setViewEnabled(previous)
                    }
                    binding?.submit?.setOnClickListener {
                        saveAnswer(currentItem, quesAdapter)
                        binding?.vm?.submitData(item.testId?.toString() ?: "0")
                    }
                }
            }
        }
    }

    val dialog by lazy {
        Dialog(this).apply { setContentView(R.layout.dialog_review_list) }
    }

    private fun showReviewDialog() {

        val rv = dialog.findViewById<RecyclerView>(R.id.rv_review)
        rv.apply {
            //setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 4)

            val rvAdapter = binding?.vm?.questionList?.get()?.let { ReviewAdapter(it, this@QuestionActivity) }
            rv.adapter = rvAdapter
            dialog.show()
        }
    }

    fun setQuestionOnUi(currentItem: Int) {
        val quesnList = binding?.vm?.questionList?.get() ?: return

        if (currentItem == 0) {
            setViewDisabled(previous)
        } else
            setViewEnabled(previous)

        if (currentItem == quesnList.size - 1) {
            setViewDisabled(next)
            binding?.submit?.visibility = View.VISIBLE
        } else {
            setViewEnabled(next)
            binding?.submit?.visibility = View.GONE
        }


        binding?.txtQuesnCounter?.text = "${(currentItem + 1)} / ${quesnList.size}"

        val quesn = quesnList.get(currentItem)
        binding?.vm?.currentQuestion?.set(quesn)
        binding?.toggleReview?.text = if (quesn.statusReview == "0") "Mark as Review" else "Marked as Review"
    }

    private fun saveAnswer(
        currentItem: Int,
        quesAdapter: QuestionAdapter
    ) {

        quesAdapter.fragList[currentItem].binding?.apply {
            //     val ans = "${if (chkA.isChecked) "1" else "0"}${if (chkB.isChecked) "1" else "0"}${if (chkC.isChecked) "1" else "0"}${if (chkD.isChecked) "1" else "0"}"
            val ans =
                "${if (chkA.isChecked) "1," else ""}${if (chkB.isChecked) "2," else ""}${if (chkC.isChecked) "3," else ""}${if (chkD.isChecked) "4," else ""}"

            binding?.vm?.questionList?.get()?.let {
                val tempList = it
                tempList?.get(currentItem)?.answer = ans.substringBeforeLast(",")
                binding?.vm?.questionList?.set(tempList)

                val unattemptedCount = tempList?.filter { it.answer == "0000" || it.answer == "" }?.size
                binding?.txtUnattemptedCounter?.text = "Unattempted: $unattemptedCount"
                binding?.txtAttemptedCounter?.text = "Attempted: ${tempList.size - unattemptedCount}"

            }
        }
    }


    fun setViewDisabled(view: View) {
        view.apply {
            isEnabled = false
            setBackgroundColor(ContextCompat.getColor(this@QuestionActivity, R.color.material_color_red200))
        }
    }

    fun setViewEnabled(view: View) {
        view.apply {
            isEnabled = true
            setBackgroundColor(ContextCompat.getColor(this@QuestionActivity, R.color.colorPrimary))
        }
    }

    fun showToast(msg: String) {
        toast(msg)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
        /*alert("Do you want to submit your test?") {
            title = "Submit"
            yesButton {
                viewModel.onPause()
            }
            noButton { }
        }.show()*/
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
        countDownTimer?.cancel()
        countDownTimerRandom?.cancel()
        updateHandler.removeCallbacks(runnable)
    }

    private fun initToolBar() {
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar)
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
//        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        alert(
            "Submit the test."
        ) {
            yesButton {
                binding?.submit?.performClick()
            }
            noButton { }
        }.show()
    }

}
