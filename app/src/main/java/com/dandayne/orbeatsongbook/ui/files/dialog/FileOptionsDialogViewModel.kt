package com.dandayne.orbeatsongbook.ui.files.dialog

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.ui.setlists.SetlistsDataHolder
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class FileOptionsDialogViewModel : ViewModel(), KoinComponent {
    private val databaseManager: DatabaseManager by inject()
    private val setlistsDataHolder: SetlistsDataHolder by inject()


    fun removeFileFromSetlist(fileUri: Uri, setlistId: Int) {
        databaseManager.removeFileFromSetlist(fileUri, setlistId)
        setlistsDataHolder.openSetlist(setlistId)
    }
}