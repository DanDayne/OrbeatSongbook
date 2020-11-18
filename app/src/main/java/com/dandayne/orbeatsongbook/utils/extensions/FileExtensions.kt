package com.dandayne.orbeatsongbook.utils.extensions

import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun File.getRoot(): File {
    return if (parentFile == null || parentFile.totalSpace != totalSpace || !parentFile.canRead())
        this else parentFile.getRoot()
}