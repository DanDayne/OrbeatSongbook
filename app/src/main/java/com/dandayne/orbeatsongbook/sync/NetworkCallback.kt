package com.dandayne.orbeatsongbook.sync


import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
open class NetworkCallback(
    private val connectivityManager: ConnectivityManager,
    private val networkRequest: NetworkRequest
) : ConnectivityManager.NetworkCallback() {
    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = _state

    override fun onAvailable(network: Network) { _state.postValue(true) }

    override fun onUnavailable() { _state.postValue(false) }

    override fun onLost(network: Network) { _state.postValue(false) }

    fun register() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    fun unregister() { connectivityManager.unregisterNetworkCallback(this) }
}