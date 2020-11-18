package com.dandayne.orbeatsongbook.utils

import android.os.Looper
import androidx.lifecycle.MutableLiveData

class LiveDataHelper {

    fun <T> setOrPostValue(target: MutableLiveData<T>, value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            target.value = value
        } else {
            target.postValue(value)
        }
    }
}