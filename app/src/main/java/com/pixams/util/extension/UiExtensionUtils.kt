package com.pixams.util.extension

/**
 * @author Kaiser Perwez
 */
 
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.LayoutRes


fun getPixelsFromDp(context: Context, dp: Float): Int {
    return Math.round(dp * context.resources.displayMetrics.density)
}

fun Activity?.removeStatusBar(hasFocus: Boolean) {
    if (hasFocus) {
        this?.let {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}

fun Activity?.setStatusBarColor(color: Int) {
    this?.let {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(color)
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(layoutRes, this, attachToRoot)
    //how to use---------->parent.inflate(R.layout.my_layout, true)
}

fun ImageView.loadUrl(url: String) {
    //Picasso.with(this.context).load(url).into(this)
}

