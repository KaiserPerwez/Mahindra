package com.android.mahindra.ui.screen.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.mahindra.Adapter.MyAdapter
import com.android.mahindra.R
import com.android.mahindra.data.model.api.ExamsModel
import com.android.mahindra.data.model.api.UserLoginData
import com.android.mahindra.data.remote.api.ApiService
import com.android.mahindra.data.remote.api.ApiService.Companion.BASE_URL
import com.android.mahindra.data.remote.api.ApiService.Companion.BASE_URL_FILE
import com.android.mahindra.ui.screen.login.LoginActivity
import com.android.mahindra.util.GlideApp
import com.android.mahindra.util.extension.isDeviceOnline
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.*
import kotlinx.android.synthetic.main.nav_header_home.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var disposable: Disposable? = null
    private val apiService by lazy { ApiService.create() }

    lateinit var loginData: UserLoginData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)


        loginData = intent.getParcelableExtra("result")

        tabLayout?.apply {
            addTab(newTab().setText("Up Coming"))
            addTab(newTab().setText("History"))
            tabGravity = TabLayout.GRAVITY_FILL
        }


        /* val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
         val navView: NavigationView = findViewById(R.id.nav_view)*/
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout?.addDrawerListener(toggle)
        toggle.syncState()

        nav_view?.setNavigationItemSelectedListener(this)

    }

    override fun onResume() {
        super.onResume()
        loginData?.apply {
            fetchExams(sapCode ?: "")

            val navHeader = nav_view?.getHeaderView(0)
            val imageView = navHeader?.findViewById<ImageView>(R.id.imageCandidate)

            imageView?.let {
                GlideApp.with(this@HomeActivity)
                    .load("$BASE_URL_FILE$profilePic").placeholder(R.mipmap.ic_launcher_round).circleCrop()
                    .into(it)
            }

            navHeader?.findViewById<TextView>(R.id.nameCandidate)?.text = "$firstName $lastName"
        }
    }

    private fun setUpViewPager(list: List<ExamsModel>) {
        val adapter = MyAdapter(this, supportFragmentManager, tabLayout!!.tabCount, list, loginData)
        viewPager?.adapter = adapter

        viewPager?.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                tabLayout
            )
        )

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }*/

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_setting -> {
                // Handle the camera action
            }
            R.id.nav_logout -> {
                startActivity(intentFor<LoginActivity>())
                finish()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun showToast(msg: String) {
        toast(msg)
    }

    fun fetchExams(sapCode: String) {
        if (!isDeviceOnline()) {
            showToast("No internet connection.")
            return
        }

        val dialog = indeterminateProgressDialog("Loading data...").apply {
            setCancelable(false)
        }

        val disposable = apiService.getExams(sapCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                runOnUiThread { dialog.show() }
            }
            .doAfterTerminate {
                runOnUiThread { dialog.dismiss() }
            }
            .subscribe(
                { result ->
                    let {
                        /*  if (result.status == Status.SUCCESS) {
                              if (result.isFirstLogin == true) {
                                  it.startActivity<RegisterActivity>("result" to result)
                              } else {
                                  it.startActivity<HomeActivity>("result" to result)
                              }
                          } else {
                              it.showToast(result.message ?: "")
                          }*/
                        result?.data?.let {
                            if (it.isNotEmpty()) {
                                setUpViewPager(it)
                            }
                        }
                    }
                },
                { error ->
                    showToast(error.message ?: "Error while fetching data")
                }
            )
    }
}
