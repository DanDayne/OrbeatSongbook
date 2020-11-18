package com.dandayne.orbeatsongbook.utils.extensions

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode

fun setNightMode(switch: Boolean) = setDefaultNightMode(
    if (switch) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
)
