package com.dandayne.orbeatsongbook.permissions

data class PermissionDeniedResult(
    val deniedRationale: List<String>,
    val deniedForever: List<String>
)