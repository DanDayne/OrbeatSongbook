package com.dandayne.orbeatsongbook.ui.setlists.dialog

import androidx.lifecycle.ViewModel
import com.dandayne.orbeatsongbook.db.DatabaseManager
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SetlistConfirmDeleteDialogViewModel: ViewModel(), KoinComponent {
    private val databaseManager: DatabaseManager by inject()

    var setlistId: Int? = null

    fun deleteSetlist() { setlistId?.let { databaseManager.deleteSetlist(it) } }
}