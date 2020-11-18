package com.dandayne.orbeatsongbook.sync

import android.content.Context
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ReactiveNetworkMonitor(context: Context): NetworkMonitor(context) {

    private val wifiRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private val cellularRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val wifiCallback = NetworkCallback(connectivityManager, wifiRequest)
    private val cellularCallback = NetworkCallback(connectivityManager, cellularRequest)

    override fun start() {
        super.start()
        wifiCallback.register()
        wifiCallback.state.observeForever(observer)
        cellularCallback.register()
        cellularCallback.state.observeForever(observer)
    }

    override fun stop() {
        super.stop()
        wifiCallback.unregister()
        wifiCallback.state.removeObserver(observer)
        cellularCallback.unregister()
        cellularCallback.state.removeObserver(observer)
    }
}