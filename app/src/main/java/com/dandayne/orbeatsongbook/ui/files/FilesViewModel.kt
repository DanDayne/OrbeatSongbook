package com.dandayne.orbeatsongbook.ui.files

import androidx.lifecycle.ViewModel
import com.dandayne.orbeatsongbook.storage.StorageManager
import com.dandayne.orbeatsongbook.utils.extensions.rotate
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class FilesViewModel : ViewModel(), KoinComponent {
    private val storageManager: StorageManager by inject()
    private val filesDataHolder: FilesDataHolder by inject()

    init {
        filesDataHolder.start()
    }

    fun reloadFiles(view: ExtendedFloatingActionButton) {
        view.rotate()
        storageManager.reloadFiles()
    }

    override fun onCleared() {
        filesDataHolder.stop()
        super.onCleared()
    }
}