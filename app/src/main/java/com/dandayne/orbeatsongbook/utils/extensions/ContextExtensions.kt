package com.dandayne.orbeatsongbook.utils.extensions

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Environment
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.os.EnvironmentCompat
import com.dandayne.orbeatsongbook.ui.settings.items.SettingsDarkModeSwitch

fun Context.getAllStoragePaths() = ContextCompat.getExternalCacheDirs(this).mapNotNull {
    if (Environment.MEDIA_MOUNTED == EnvironmentCompat.getStorageState(it)) it.getRoot() else null
}

fun Context.isNightModeForced() =
    getSharedPreferences(
        "Settings",
        Application.MODE_PRIVATE
    ).getInt(SettingsDarkModeSwitch.NIGHT_MODE, MODE_NIGHT_FOLLOW_SYSTEM) == MODE_NIGHT_YES

fun Context.isSystemNightModeEnabled() =
    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> false
    }

fun Context.isNightModeEnabled() =
    if (isNightModeForced()) true else isSystemNightModeEnabled()

fun Context.saveNightModeSwitch(switch: Boolean) {
    val mode = if (switch) MODE_NIGHT_YES else MODE_NIGHT_FOLLOW_SYSTEM
    getSharedPreferences("Settings", Application.MODE_PRIVATE).edit(commit = true) {
        putInt(SettingsDarkModeSwitch.NIGHT_MODE, mode)
    }
}
