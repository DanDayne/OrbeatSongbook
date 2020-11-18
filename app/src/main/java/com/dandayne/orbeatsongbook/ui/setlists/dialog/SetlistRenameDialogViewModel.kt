package com.dandayne.orbeatsongbook.ui.setlists.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.utils.LiveDataHelper
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SetlistRenameDialogViewModel : ViewModel(), KoinComponent {
    private val databaseManager: DatabaseManager by inject()
    private val liveDataHelper: LiveDataHelper by inject()

    var setlistId: Int? = null

    private val _close = MutableLiveData(false)
    val close: LiveData<Boolean> = _close

    private val _error = MutableLiveData<Int?>()
    val error: LiveData<Int?> = _error

    fun rename(name: String?) {
        setlistId?.let {
            if (name.isNullOrEmpty()) {
                onBlankName()
                return
            }
            if (databaseManager.renameSetlist(it, name)) close() else onNameTaken()
        }
    }

    private fun close() {
        liveDataHelper.setOrPostValue(_close, true)
    }

    private fun onBlankName() {
        liveDataHelper.setOrPostValue(_error, R.string.empty_string_error)
    }

    private fun onNameTaken() {
        liveDataHelper.setOrPostValue(_error, R.string.setlist_name_taken)
    }
}