package com.dandayne.orbeatsongbook.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dandayne.orbeatsongbook.db.DatabaseManager
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

open class AbstractRecyclerDataHolder<T : Any> : KoinComponent {

    protected val databaseManager: DatabaseManager by inject()
    protected val liveDataHelper: LiveDataHelper by inject()

    private val _shown = MutableLiveData<List<T>>(listOf())
    val shown: LiveData<List<T>> = _shown

    protected val observer = Observer<List<T>> { updateShown(it) }

    open fun updateShown(items: List<T>) { liveDataHelper.setOrPostValue(_shown, items) }
}