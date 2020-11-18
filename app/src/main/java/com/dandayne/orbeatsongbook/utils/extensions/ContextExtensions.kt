package com.dandayne.orbeatsongbook.utils.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.os.EnvironmentCompat

fun Context.isPermissionGranted(permission: String) =
    PermissionChecker.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED

fun Context.areAllPermissionsGranted() =
    getAllPermissionsFromManifest().all { isPermissionGranted(it) }.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            Environment.isExternalStorageManager() && it
        else it
    }

fun Context.getAllPermissionsFromManifest(): List<String> {
    @SuppressLint("InlinedApi")
    val specialPermissions = listOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    val allPermissions = packageManager
        .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        ?.requestedPermissions
        ?.toMutableList()
        ?: mutableListOf()

    return allPermissions.filterNot { specialPermissions.contains(it) }
}

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