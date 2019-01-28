package org.timreynolds.imagesearch.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import org.timreynolds.imagesearch.R


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ImageView.loadImage(imageUrl: String?) {
    Picasso.get().load(imageUrl)
            .placeholder(R.drawable.ic_image_black_placeholder)
            .error(R.drawable.ic_broken_image_black)
            .into(this)
}

inline fun <reified T : Any> Context.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/**
 * TAG utility for logging relevant class name
 * NOTE: This does not work being called from AppCompat Activity
 */
val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun Activity.logdebug(message: String){
    if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, message)
}

fun Adapter.logdebug(message: String){
    if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, message)
}