package com.dandayne.orbeatsongbook.ui.files

import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.utils.AbstractRecyclerDataHolder
import java.text.Normalizer
import java.util.*

class FilesDataHolder: AbstractRecyclerDataHolder<File>() {

    fun start() = databaseManager.files.observeForever(observer)
    fun stop() = databaseManager.files.removeObserver(observer)

    override fun updateShown(items: List<File>) {
        val sortedItems = items.sortedBy {
            Normalizer.normalize(
                it.fileName.toLowerCase(Locale.ROOT),
                Normalizer.Form.NFD
            )
        }
        super.updateShown(sortedItems)
    }
}