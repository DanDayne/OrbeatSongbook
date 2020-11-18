package com.dandayne.orbeatsongbook.utils.extensions

import android.animation.ObjectAnimator
import android.view.View
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

fun ExtendedFloatingActionButton.rotate() {
    val animation: ObjectAnimator =
        ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 360f)
    animation.duration = 2000
    animation.start()
}