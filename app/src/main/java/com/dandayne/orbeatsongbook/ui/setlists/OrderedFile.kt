package com.dandayne.orbeatsongbook.ui.setlists

import com.dandayne.orbeatsongbook.db.model.File

class OrderedFile(
    val file: File,
    val order: Int
)