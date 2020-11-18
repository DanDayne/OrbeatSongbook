package com.dandayne.orbeatsongbook.utils.extensions

import android.app.Activity
import android.content.Intent

fun Activity.restartActivity() {
    startActivity(Intent(this, this::class.java))
    finish()
}