package com.pixams.util.extension

/**
 * @author Kaiser Perwez
 */

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import com.pixams.data.local.prefs.PreferenceHelper
import com.pixams.ui.screen.login.LoginActivity
//import com.google.android.exoplayer2.DefaultLoadControl
//import com.google.android.exoplayer2.DefaultRenderersFactory
//import com.google.android.exoplayer2.ExoPlayerFactory
//import com.google.android.exoplayer2.source.ExtractorMediaSource
//import com.google.android.exoplayer2.source.MediaSource
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
//import com.google.android.exoplayer2.ui.PlayerView
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.net.URLConnection


var globalEmail: String? = null

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

fun Context?.isDeviceOnline(): Boolean {
    var connected = false
    try {
        val connectivityManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
        return connected

    } catch (e: Exception) {
        this?.toast(e.message ?: "")
    }

    return connected
}

fun Context.dialCallIntent(phone: String) {
    //use anko if u need to place a call directly without showing dial pad
    try {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        this.startActivity(intent)
    } catch (ex: Exception) {
        this.toast(ex.message.toString())
    }
}

fun Activity.emailIntent(email: String) {//, subject: String, message: String) {
    //use anko
}

fun Activity?.restartActivity() {
    val intent = this?.intent
    intent?.putExtra("fragment_name", "my_task")
    this?.overridePendingTransition(0, 0)
    this?.finish()

    this?.overridePendingTransition(0, 0)
    this?.startActivity(intent)
}

infix fun <T> Collection<T>.sameContentWith(collection: Collection<T>?) = collection?.let {
    this.size == it.size && this.containsAll(it)
}


fun Activity?.dismissKeyboard() {
    val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    //OR  imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
}


private fun isValidUrl(url: String): Boolean {
    val p = Patterns.WEB_URL
    val m = p.matcher(url.toLowerCase())
    return m.matches()
}

fun Activity?.logout() {
    this@logout?.alert("Sure to Logout?") {
        yesButton {
            this@logout.toast("Logging out..")
            PreferenceHelper.clearSharedPreferences(
                PreferenceHelper.getSharedPrefs(this@logout)
            )
            this@logout.startActivity<LoginActivity>()
            finish()
        }
        negativeButton("Stay") {
        }
    }?.show()
}


fun androidx.fragment.app.FragmentActivity?.loadFragment(contentFrame: Int, fragment: androidx.fragment.app.Fragment) {
    this?.supportFragmentManager?.let {
        for (index in 0..(it.backStackEntryCount))
            it.popBackStackImmediate()

        it.beginTransaction().replace(contentFrame, fragment).commit()
    }
}

/*fun Activity?.playVideo(playerView: PlayerView, url: String) {
    val player = ExoPlayerFactory.newSimpleInstance(
        DefaultRenderersFactory(this),
        DefaultTrackSelector(), DefaultLoadControl()
    )
    playerView.player = player
    player.playWhenReady = true
    val uri = Uri.parse(url)
    val mediaSource = buildMediaSource(uri)
    player.prepare(mediaSource, true, false)
}

private fun buildMediaSource(uri: Uri): MediaSource {
    return ExtractorMediaSource.Factory(
        DefaultHttpDataSourceFactory("exoplayer-codelab")
    ).createMediaSource(uri)
}*/

fun String.isImageFile(): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && mimeType.startsWith("image")
}

fun String.isVideoFile(): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && mimeType.startsWith("video")
}
