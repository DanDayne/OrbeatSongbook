package com.dandayne.orbeatsongbook.utils.extensions

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.dandayne.orbeatsongbook.ui.navigation.NavigationController


fun Activity.hideSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let {
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
            it.hide(WindowInsets.Type.systemBars())
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    (this as? NavigationController)?.toggleNavigationBar(false)
}

fun Activity.showSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.show(WindowInsets.Type.systemBars())
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    (this as? NavigationController)?.toggleNavigationBar(true)
}
