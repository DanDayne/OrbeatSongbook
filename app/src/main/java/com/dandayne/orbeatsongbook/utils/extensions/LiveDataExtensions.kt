package com.dandayne.orbeatsongbook.utils.extensions


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <S1, S2, R> LiveData<S1>.combineWith(liveData: LiveData<S2>, block: (S1?, S2?) -> R): LiveData<R> {
    return MediatorLiveData<R>().apply {
        var firstSourceEmit = false
        var secondSourceEmit = false

        fun combine() {
            if (firstSourceEmit && secondSourceEmit) value = block(this@combineWith.value, liveData.value)
        }

        addSource(this@combineWith) {
            firstSourceEmit = true
            combine()
        }
        addSource(liveData) {
            secondSourceEmit = true
            combine()
        }
    }
}

fun <S1, S2, S3, R> LiveData<S1>.combineWith(
    secondLiveData: LiveData<S2>,
    thirdLiveData: LiveData<S3>,
    block: (S1?, S2?, S3?) -> R
): LiveData<R> {
    return MediatorLiveData<R>().apply {
        var firstSourceEmit = false
        var secondSourceEmit = false
        var thirdSourceEmit = false

        fun combine() {
            if (firstSourceEmit && secondSourceEmit && thirdSourceEmit) {
                value = block(this@combineWith.value, secondLiveData.value, thirdLiveData.value)
            }
        }

        addSource(this@combineWith) {
            firstSourceEmit = true
            combine()
        }
        addSource(secondLiveData) {
            secondSourceEmit = true
            combine()
        }
        addSource(thirdLiveData) {
            thirdSourceEmit = true
            combine()
        }
    }
}

fun <S1, S2, R> LiveData<S1>.mergeWith(liveData: LiveData<S2>, block: (S1?, S2?) -> R): LiveData<R> {
    return MediatorLiveData<R>().apply {

        fun combine() {
            value = block(this@mergeWith.value, liveData.value)
        }

        addSource(this@mergeWith) { combine() }
        addSource(liveData) { combine() }
    }
}

fun <S1, S2, S3, R> LiveData<S1>.mergeWith(
    secondLiveData: LiveData<S2>,
    thirdLiveData: LiveData<S3>,
    block: (S1?, S2?, S3?) -> R
): LiveData<R> {
    return MediatorLiveData<R>().apply {

        fun combine() {
            value = block(this@mergeWith.value, secondLiveData.value, thirdLiveData.value)
        }

        addSource(this@mergeWith) { combine() }
        addSource(secondLiveData) { combine() }
        addSource(thirdLiveData) { combine() }
    }
}

fun <T> LiveData<T>.filter(predicate: (T?) -> Boolean): LiveData<T> {
    return MediatorLiveData<T>().apply {
        addSource(this@filter) {
            if (predicate(it)) value = it
        }
    }
}