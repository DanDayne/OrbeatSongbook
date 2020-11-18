package com.dandayne.orbeatsongbook.ui.pdf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.sync.SyncManager
import com.dandayne.orbeatsongbook.utils.LiveDataHelper
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class PdfDataHolder : KoinComponent {

    private val liveDataHelper: LiveDataHelper by inject()
    private val syncManager: SyncManager by inject()

    private val _openedFile = MutableLiveData<FileToLaunch>()
    val openedFile: LiveData<FileToLaunch> = _openedFile

    private val _queue = MutableLiveData<List<File>>()
    val queue: LiveData<List<File>> = _queue

    private val _hasNext = MutableLiveData<Boolean>()
    val hasNext: LiveData<Boolean> = _hasNext

    private val _hasPrevious = MutableLiveData<Boolean>()
    val hasPrevious: LiveData<Boolean> = _hasPrevious

    fun openFile(newQueue: List<File>? = null, queuePosition: Int, sync: Boolean = true) {
        newQueue?.let { updateQueue(newQueue) }
        liveDataHelper.setOrPostValue(_hasPrevious, queuePosition != 0)

        liveDataHelper.setOrPostValue(
            _hasNext,
            queuePosition != queue.value?.size?.minus(1) ?: 0
        )

        queue.value?.getOrNull(queuePosition)?.let {
            if (sync) syncManager.sendUDPMessage(it.fileName, Math.random())
            liveDataHelper.setOrPostValue(_openedFile, FileToLaunch(it))
        }
    }

    fun openNextFile() {
        openedFile.value?.file?.let { file ->
            queue.value?.indexOf(file)?.let { index ->
                openFile(queuePosition = index + 1)
            }
        }
    }

    fun openPreviousFile() {
        openedFile.value?.file?.let { file ->
            queue.value?.indexOf(file)?.let { index ->
                openFile(queuePosition = index - 1)
            }
        }
    }

    private fun updateQueue(newQueue: List<File>) {
        liveDataHelper.setOrPostValue(_queue, newQueue)
    }
}