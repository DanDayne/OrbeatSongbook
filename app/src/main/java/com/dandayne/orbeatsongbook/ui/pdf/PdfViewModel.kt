package com.dandayne.orbeatsongbook.ui.pdf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dandayne.orbeatsongbook.utils.LiveDataHelper
import com.dandayne.orbeatsongbook.utils.extensions.mergeWith
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class PdfViewModel : ViewModel(), KoinComponent {
    private val dataHolder: PdfDataHolder by inject()
    private val liveDataHelper: LiveDataHelper by inject()

    private val _isInfoVisible = MutableLiveData(true)
    val isInfoVisible: LiveData<Boolean> = _isInfoVisible

    private var infoVisibilityJob: Job? = null

    val openedFile = dataHolder.openedFile
    val hasNext = dataHolder.hasNext.mergeWith(isInfoVisible) { hasNext, isInfoVisible ->
        return@mergeWith if (hasNext == null && isInfoVisible == null) null
        else if (hasNext == null) isInfoVisible
        else if (isInfoVisible == null) hasNext
        else hasNext && isInfoVisible
    }

    val hasPrevious =
        dataHolder.hasPrevious.mergeWith(isInfoVisible) { hasPrevious, isInfoVisible ->
            return@mergeWith if (hasPrevious == null && isInfoVisible == null) null
            else if (hasPrevious == null) isInfoVisible
            else if (isInfoVisible == null) hasPrevious
            else hasPrevious && isInfoVisible
        }

    companion object {
        private const val INFO_TIMEOUT = 5000L
    }

    fun changeInfoVisibility() {
        infoVisibilityJob?.cancel()
        (isInfoVisible.value?: true).let {
            liveDataHelper.setOrPostValue(_isInfoVisible, !it)
            if (!it) {
                changeInfoVisibilityWithDelay()
            }
        }
    }

    fun changeInfoVisibilityWithDelay() {
        infoVisibilityJob?.cancel()
        infoVisibilityJob = GlobalScope.launch(Dispatchers.IO) {
            delay(INFO_TIMEOUT)
            changeInfoVisibility()
        }
    }

    fun infoToPermanentlyVisible() {
        liveDataHelper.setOrPostValue(_isInfoVisible, true)
        infoVisibilityJob?.cancel()
    }

    fun nextFile() {
        dataHolder.openNextFile()
        changeInfoVisibilityWithDelay()
    }
    fun previousFile() {
        dataHolder.openPreviousFile()
        changeInfoVisibilityWithDelay()
    }
}