package com.android.mahindra.ui.screen.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.Status
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.data.remote.api.ApiService.Companion.BASE_URL_FILE
import com.android.mahindra.databinding.ActivityHomeBinding
import com.android.mahindra.ui.screen.login.LoginActivity
import com.android.mahindra.ui.screen.settings.SettingsActivity
import com.android.mahindra.ui.screen.start_test.StartTestActivity
import com.android.mahindra.ui.screen.validate.ValidateActivity
import com.android.mahindra.util.GlideApp
import com.android.mahindra.util.KEY_INTENT_EXAM_MODEL
import com.android.mahindra.util.KEY_INTENT_LOGIN_DATA
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }

    val loginData by lazy {
        intent.getParcelableExtra(KEY_INTENT_LOGIN_DATA) as UserLoginData
    }

    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        setSupportActionBar(toolbar)
        binding?.apply {
            vm = HomeViewModel(this@HomeActivity)
            appBarHome?.tabLayout?.apply {
                addTab(newTab().setText("Up Coming"))
                addTab(newTab().setText("History"))
                tabGravity = TabLayout.GRAVITY_FILL
            }

            val toggle = ActionBarDrawerToggle(
                this@HomeActivity, drawer_layout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawerLayout?.addDrawerListener(toggle)
            toggle.syncState()

            navView?.setNavigationItemSelectedListener(this@HomeActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        loginData.apply {
            binding?.vm?.fetchExams(loginData.sapCode ?: "")

            val navHeader = binding?.navView?.getHeaderView(0)
            val imageView = navHeader?.findViewById<ImageView>(R.id.imageCandidate)

            imageView?.let {
                GlideApp.with(this@HomeActivity)
                    .load("$BASE_URL_FILE$profilePic").placeholder(R.mipmap.ic_launcher_round)
                    .circleCrop()
                    .into(it)
            }

            navHeader?.findViewById<TextView>(R.id.nameCandidate)?.text = "$emp_name"
        }
    }

    fun setUpViewPager(list: List<ExamsModel>) {
        //TODO: create a binding adapter later
        binding?.appBarHome?.apply {
            val adapter = MyAdapter(
                this@HomeActivity,
                supportFragmentManager,
                tabLayout.tabCount,
                list,
                loginData
            )
            viewPager?.adapter = adapter

            viewPager?.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(tabLayout)
            )

            tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })
        }
    }


    override fun onBackPressed() {
        binding?.drawerLayout?.apply {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_setting -> {
                startActivity<SettingsActivity>(KEY_INTENT_LOGIN_DATA to loginData)
            }
            R.id.nav_logout -> {
                startActivity(intentFor<ValidateActivity>())
                finish()
            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    fun validateTest(item: ExamsModel?) {
        val dialog = indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }

        disposable = apiService.validateScheduledDatetime(
            loginData.sapCode ?: "",
            item?.testId?.toString() ?: "",
            item?.scheduled_id?.toString() ?: ""
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { runOnUiThread { dialog.show() } }
            .doAfterTerminate { runOnUiThread { dialog.dismiss() } }
            .subscribe(
                { result ->
                    if (result.status == Status.SUCCESS) {
                        startActivity<StartTestActivity>(
                            KEY_INTENT_EXAM_MODEL to item,
                            KEY_INTENT_LOGIN_DATA to loginData
                        )
                    } else
                        toast(result.message ?: "Can not give test now")
                },
                { error ->
                    toast(error.message ?: "Error while fetching data")
                }
            )
    }

    override fun onPause() {
        super.onPause()
        binding?.vm?.onPause()
    }

    fun showToast(msg: String) {
        toast(msg)
    }

}
