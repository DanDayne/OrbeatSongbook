package com.dandayne.orbeatsongbook.ui.setlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dandayne.orbeatsongbook.db.model.Setlist
import com.dandayne.orbeatsongbook.db.model.SetlistWithFiles
import com.dandayne.orbeatsongbook.utils.AbstractRecyclerDataHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Normalizer
import java.util.*

class SetlistsDataHolder : AbstractRecyclerDataHolder<Setlist>() {

    private val _showingSetlistContent = MutableLiveData<SetlistWithFiles?>(null)
    val showingSetlistContent: LiveData<SetlistWithFiles?> = _showingSetlistContent

    private val _showingProgressBar = MutableLiveData<Boolean>(false)
    val showingProgressBar: LiveData<Boolean> = _showingProgressBar

    fun start() {
        databaseManager.setlists.observeForever(observer)
    }

    fun stop() {
        databaseManager.setlists.removeObserver(observer)
    }

    fun refresh() {
        showingSetlistContent.value?.let { openSetlist(it.setlist.setlistId) }
    }

    fun openSetlist(setlistId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val setlistWithFiles = databaseManager.fetchSetlistWithFiles(setlistId)
            withContext(Dispatchers.Main) {
                liveDataHelper.setOrPostValue(_showingSetlistContent, setlistWithFiles)
            }
        }
    }

    fun closeSetlist() {
        GlobalScope.launch(Dispatchers.Main) {
            liveDataHelper.setOrPostValue(_showingSetlistContent, null)
        }
    }

    override fun updateShown(items: List<Setlist>) {
        val sortedItems = items.sortedBy {
            Normalizer.normalize(
                it.setlistName.toLowerCase(Locale.ROOT),
                Normalizer.Form.NFD
            )
        }
        super.updateShown(sortedItems)
    }

    fun toggleProgressBar(toggle: Boolean) {
        liveDataHelper.setOrPostValue(_showingProgressBar, toggle)
    }
}