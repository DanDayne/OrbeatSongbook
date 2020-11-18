package com.dandayne.orbeatsongbook.ui.setlists.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.utils.LiveDataHelper
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NewSetlistDialogViewModel : ViewModel(), KoinComponent {

    private val databaseManager: DatabaseManager by inject()
    private val liveDataHelper: LiveDataHelper by inject()

    private val _close = MutableLiveData(false)
    val close: LiveData<Boolean> = _close

    private val _error = MutableLiveData<Int?>()
    val error: LiveData<Int?> = _error

    fun newSetlist(name: String?) {
        when {
            name.isNullOrEmpty() -> onBlankSetlistName()
            databaseManager.newSetlist(name) == null -> onSetlistNameTaken()
            else -> close()
        }
    }

    private fun close() {
        liveDataHelper.setOrPostValue(_close, true)
    }

    private fun onBlankSetlistName() {
        liveDataHelper.setOrPostValue(_error, R.string.empty_string_error)
    }

    private fun onSetlistNameTaken() {
        liveDataHelper.setOrPostValue(_error, R.string.setlist_name_taken)
    }
}