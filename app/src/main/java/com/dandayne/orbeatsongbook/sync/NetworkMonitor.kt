package com.dandayne.orbeatsongbook.sync

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import java.net.InetAddress
import java.net.InterfaceAddress
import java.net.NetworkInterface
import java.util.*

abstract class NetworkMonitor(private val context: Context) {

    companion object {
        internal const val WIFI_AP_STATE_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED"
        internal const val JAVA_PROPERTY = "java.net.preferIPv4Stack"
        internal const val UPDATE_NETWORK_STATE_DELAY = 2000L
    }

    private val hotspotManager: TetheringReceiver = TetheringReceiver()

    internal val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _broadcastIP = MutableLiveData<String?>()
    val broadcastIP: LiveData<String?> = _broadcastIP

    internal val observer = Observer<Boolean> {
        CoroutineScope(Dispatchers.IO).launch { updateNetworkState() }
    }

    internal suspend fun updateNetworkState() {
        delay(UPDATE_NETWORK_STATE_DELAY)
        reloadBroadcastAddress().let {
            if (it != broadcastIP.value) {
                _broadcastIP.postValue(it)
            }
        }
    }

    private fun reloadBroadcastAddress(): String? {
        System.setProperty(JAVA_PROPERTY, true.toString())

        val interfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()

        var broadcastAddress: InetAddress

        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()

            if (networkInterface.isLoopback) {
                continue
            }

            for (interfaceAddress: InterfaceAddress in networkInterface.interfaceAddresses) {
                runCatching {
                    broadcastAddress = interfaceAddress.broadcast
                    broadcastAddress.let {
                        return it.toString().substring(1)
                    }
                }
            }
        }
        return null
    }

    open fun start() {
        val mIntentFilter = IntentFilter(WIFI_AP_STATE_CHANGED)
        context.registerReceiver(hotspotManager, mIntentFilter)
        hotspotManager.state.observeForever(observer)
    }

    open fun stop() {
        hotspotManager.state.removeObserver(observer)
        context.unregisterReceiver(hotspotManager)
    }
}