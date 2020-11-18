package com.dandayne.orbeatsongbook.ui.pdf

import com.dandayne.orbeatsongbook.db.model.File

class FileToLaunch(val file: File) {
    var hasBeenHandled: Boolean = false
        private set

    fun handle() {
        hasBeenHandled = true
    }
}