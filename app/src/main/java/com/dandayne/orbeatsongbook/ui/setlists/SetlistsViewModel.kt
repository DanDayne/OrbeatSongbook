package com.dandayne.orbeatsongbook.ui.setlists

import androidx.lifecycle.ViewModel
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SetlistsViewModel : ViewModel(), KoinComponent {

    private val setlistsDataHolder: SetlistsDataHolder by inject()
    val showingSetlistContent = setlistsDataHolder.showingSetlistContent
    val showingProgressBar = setlistsDataHolder.showingProgressBar

    init {
        setlistsDataHolder.start()
    }

    override fun onCleared() {
        setlistsDataHolder.stop()
        super.onCleared()
    }

    fun refresh() = setlistsDataHolder.refresh()

    fun closeSetlist() { setlistsDataHolder.closeSetlist() }
}