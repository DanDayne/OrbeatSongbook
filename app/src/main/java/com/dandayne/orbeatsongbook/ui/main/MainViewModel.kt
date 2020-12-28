package com.dandayne.orbeatsongbook.ui.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.sync.MessageDisplay
import com.dandayne.orbeatsongbook.sync.SyncManager
import com.dandayne.orbeatsongbook.ui.pdf.PdfDataHolder
import com.dandayne.orbeatsongbook.ui.setlists.SetlistsDataHolder
import com.dandayne.orbeatsongbook.utils.LiveDataHelper
import com.dandayne.orbeatsongbook.utils.extensions.mergeWith
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MainViewModel : ViewModel(), KoinComponent {



    private val liveDataHelper: LiveDataHelper by inject()
    private val pdfDataHolder: PdfDataHolder by inject()
    private val databaseManager: DatabaseManager by inject()
    private val syncManager: SyncManager by inject()
    private val setlistsDataHolder: SetlistsDataHolder by inject()

    private val _message = MutableLiveData<Int>()
    val message: LiveData<Int> = _message

    private val _isOnSetlistsFragment = MutableLiveData<Boolean>()
    private val isOnSetlistsFragment: LiveData<Boolean> = _isOnSetlistsFragment

    val actionBarTitle = setlistsDataHolder.showingSetlistContent.mergeWith(isOnSetlistsFragment)
    { setlistWithFiles, isOnSetlistFragment ->
        if (isOnSetlistFragment == true) setlistWithFiles?.setlist?.setlistName else null
    }

    @ObsoleteCoroutinesApi
    fun setupSyncManager(messageDisplay: MessageDisplay, lifecycleOwner: LifecycleOwner) {
        syncManager.messageDisplay = messageDisplay
        syncManager.start()
        syncManager.fileNameToLaunch.observe(lifecycleOwner) {
            if (!it.hasBeenHandled) {
                databaseManager.files.value?.find { file -> file.fileName == it.name }
                    ?.let { file ->
                        pdfDataHolder.openFile(listOf(file), 0, sync = false)
                    } ?: _message.postValue(R.string.non_existent_file)
            }
        }
    }

    fun closeSetlist() {
        setlistsDataHolder.closeSetlist()
    }

    fun setIsOnSetlistFragment(value: Boolean) {
        liveDataHelper.setOrPostValue(_isOnSetlistsFragment, value)
    }
}