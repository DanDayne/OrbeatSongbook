package com.dandayne.orbeatsongbook.utils.extensions

import android.content.Context
import android.content.res.Configuration
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.os.EnvironmentCompat

fun Context.getAllStoragePaths() = ContextCompat.getExternalCacheDirs(this).mapNotNull {
    if (Environment.MEDIA_MOUNTED == EnvironmentCompat.getStorageState(it)) it.getRoot() else null
}

fun Context.isUsingNightModeResources(): Boolean {
    return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> false
    }
}