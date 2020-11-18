package com.dandayne.orbeatsongbook.sync

class FileNameToLaunch(private val fileName: String) {
    var hasBeenHandled: Boolean = false
        private set

    val name: String
        get() {
            hasBeenHandled = true
            return fileName
        }
}