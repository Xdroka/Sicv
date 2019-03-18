package com.tcc.sicv.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tcc.sicv.R

fun View.hide() {
    visibility = GONE
}

fun View.show() {
    visibility = VISIBLE
}

fun ImageView.loadImageUrl(url: String, placeholder: Int? = R.drawable.ic_car) {
    Glide.with(context)
            .load(url)
            .apply { placeholder?.let { placeholder(it) } }
            .into(this)
}

inline fun <reified T : Activity> Activity.startActivity(
        map: Map<String, Any>? = null,
        flags: Int? = null
) {
    val intent = Intent(this, T::class.java)
    map?.forEach {
        when(it.value) {
            is Int -> intent.putExtra(it.key, it.value as Int)
            is Boolean -> intent.putExtra(it.key, it.value as Boolean)
            is Float -> intent.putExtra(it.key, it.value as Float)
            else -> intent.putExtra(it.key, it.value.toString())
        }
    }
    flags?.let { intent.flags = it }
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityAndFinish(
        map: Map<String, Any>? = null,
        flags: Int? = null
) {
    startActivity<T>(map, flags)
    finish()
}