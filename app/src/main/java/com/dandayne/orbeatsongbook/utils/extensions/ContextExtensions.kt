package com.dandayne.orbeatsongbook.utils.extensions

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Environment
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.os.EnvironmentCompat
import com.dandayne.orbeatsongbook.ui.settings.items.SettingsDarkModeSwitch

fun Context.getAllStoragePaths() = ContextCompat.getExternalCacheDirs(this).mapNotNull {
    if (Environment.MEDIA_MOUNTED == EnvironmentCompat.getStorageState(it)) it.getRoot() else null
}

fun Context.isNightModeEnabled() =
    getSharedPreferences(
        "Settings",
        Application.MODE_PRIVATE
    ).getInt(SettingsDarkModeSwitch.NIGHT_MODE, MODE_NIGHT_NO) == MODE_NIGHT_YES

fun Context.saveNightModeSwitch(switch: Boolean) {
    val mode = if (switch) MODE_NIGHT_YES else MODE_NIGHT_NO
    getSharedPreferences("Settings", Application.MODE_PRIVATE).edit(commit = true) {
        putInt(SettingsDarkModeSwitch.NIGHT_MODE, mode)
    }
}
