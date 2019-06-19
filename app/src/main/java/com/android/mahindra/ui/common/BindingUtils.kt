package com.android.mahindra.ui.common

/**
 * @author Kaiser Perwez
 */
 
import androidx.databinding.BindingAdapter
import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.android.mahindra.R
import com.android.mahindra.util.GlideApp

object BindingUtils {
    /*  @JvmStatic
      @BindingAdapter("numtext")
      fun EditText.numToText(value: Int) {
          this.setText(value.toString())
      }

      @JvmStatic
      @InverseBindingAdapter(attribute = "numtext")
      fun EditText.TextToNum(): Int {
          try {
              return this.text.toString().toInt()
          } catch (ex: Exception) {
              return 1000
          }
      }

      @JvmStatic
      @BindingAdapter("numtextAttrChanged")
      fun EditText.addListener(listener: InverseBindingListener) {
          this.addTextChangedListener(object : TextWatcher {
              override fun afterTextChanged(p0: Editable?) {

              }

              override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
              }

              override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                      if (p1!=p2 && (p2>0 || p3>0))
                          listener.onChange()
              }

          })
      }*/



    @JvmStatic
    @BindingAdapter(value = ["image_url", "circleCrop"], requireAll = false)
    fun ImageView.setImage(imageUrl: String?, circleCrop: Boolean = true) {
        if (imageUrl == null) return
        GlideApp.with(this.context).load(imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .apply{  if (circleCrop)  circleCrop()  }
            .into(this)
    }

    @JvmStatic
    @BindingAdapter(value = ["html_text"], requireAll = true)
    fun TextView.loadHtmlOnTextView(text: String?) {
        this.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text?.trim(), Html.FROM_HTML_MODE_COMPACT)
        } else// ?: "No HTML Content"
        {
            Html.fromHtml(text?.trim()) ?: ""
        }
    }

}
