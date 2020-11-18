package com.dandayne.orbeatsongbook.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class TetheringReceiver : BroadcastReceiver() {

    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = _state

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val state = it.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)
            _state.postValue(WifiManager.WIFI_STATE_ENABLED == state % 10)
        }
    }
}