package com.dandayne.orbeatsongbook.sync

import android.content.Context
import com.dandayne.orbeatsongbook.utils.extensions.launchInterval
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job

class PeriodicalNetworkMonitor(context: Context): NetworkMonitor(context) {

    companion object {
        private const val UPDATE_NETWORK_STATE_INTERVAL = 5000L
    }
    private var syncJob: Job? = null

    private fun periodicallyUpdateState() =
        GlobalScope.launchInterval(
            context = Dispatchers.IO,
            initialDelayMillis = UPDATE_NETWORK_STATE_INTERVAL
        ) { updateNetworkState() }

    override fun start() {
        super.start()
        syncJob = periodicallyUpdateState()
    }

    override fun stop() {
        super.stop()
        syncJob?.cancel()
    }
}