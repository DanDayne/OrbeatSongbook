package com.dandayne.orbeatsongbook.ui.files

import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.db.model.Setlist

interface FilesController {
    fun onFileItemHold(file: File, setlist: Setlist? = null)
}