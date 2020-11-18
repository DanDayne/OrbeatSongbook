package com.dandayne.orbeatsongbook.sync

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.utils.Converter
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.coroutines.CoroutineContext

class SyncManager : KoinComponent {
    companion object {
        private const val PORT = 11111
        private const val THREAD_CONTEXT_NAME = "UDPConnector"
        private const val END_OF_CODE = "end_of_code"
        private const val END_OF_REQUEST_CODE = "end_of_request_code"
        private const val RETRIES = 10
        private const val DELAY = 50L
        private const val RECEIVED_BUFFER_SIZE = 15000
    }

    private val networkMonitor: NetworkMonitor by inject()
    lateinit var messageDisplay: MessageDisplay

    private val id = Math.random().toString()
    private var lastRequest: String? = null
    private val bytesUtils = Converter()

    @ObsoleteCoroutinesApi
    private val udpCoroutineContext: CoroutineContext = newSingleThreadContext(THREAD_CONTEXT_NAME)

    private val _fileNameToLaunch = MutableLiveData<FileNameToLaunch>()
    val fileNameToLaunch: LiveData<FileNameToLaunch> = _fileNameToLaunch

    private var isStarted = false

    private fun listenForUDPBroadcast(job: Job) {
        Looper.prepare()
        var socket: DatagramSocket? = null
        runCatching {
            socket = DatagramSocket(PORT, InetAddress.getByName(networkMonitor.broadcastIP.value))

            while (!job.isCancelled) {
                val receivedBuffer = ByteArray(RECEIVED_BUFFER_SIZE)
                socket?.broadcast = true
                val packet = DatagramPacket(receivedBuffer, receivedBuffer.size)
                socket?.receive(packet)

                val codedMessage = bytesUtils.convert(packet.data)
                val endOfCode = codedMessage.indexOf(END_OF_CODE)
                val endOfRequestCode = codedMessage.indexOf(END_OF_REQUEST_CODE)
                val senderCode = codedMessage.substring(0, endOfCode)
                val requestCode =
                    codedMessage.substring(endOfCode + END_OF_CODE.length, endOfRequestCode)
                val message =
                    codedMessage.substring(endOfRequestCode + END_OF_REQUEST_CODE.length)

                if (id != senderCode && requestCode != lastRequest
                ) {
                    lastRequest = requestCode
                    _fileNameToLaunch.postValue(FileNameToLaunch(message))
                }
            }
        }.onFailure {
            if (it::class == CancellationException::class) socket?.close()
            Log.e(THREAD_CONTEXT_NAME, it.toString())
        }

    }

    @SuppressLint("HardwareIds")
    @Suppress("BlockingMethodInNonBlockingContext")
    fun sendUDPMessage(msg: String, requestCode: Double) {
        runCatching {
            val clientSocket = DatagramSocket()
            clientSocket.broadcast = true
            val address = InetAddress.getByName(networkMonitor.broadcastIP.value)

            val sendData =
                (id + END_OF_CODE + requestCode + END_OF_REQUEST_CODE + msg).toByteArray()

            val sendPacket = DatagramPacket(
                sendData,
                sendData.size, address, PORT
            )
            GlobalScope.launch(Dispatchers.IO) {
                for (i in 0..RETRIES) {
                    clientSocket.send(sendPacket)
                    delay(DELAY)
                }
                clientSocket.close()
            }
        }
    }

    @ObsoleteCoroutinesApi
    fun start() {
        if (!isStarted) {
            isStarted = true
            networkMonitor.start()
            var job: Job? = null

            val observer = Observer<String?> { ip ->
                if (ip != null) {

                    job = CoroutineScope(udpCoroutineContext).launch {
                        job?.let { listenForUDPBroadcast(it) }
                    }
                    messageDisplay.showMessage(R.string.network_found)

                } else {
                    job?.let {
                        if (it.isActive) {
                            CoroutineScope(udpCoroutineContext).launch {
                                it.cancelAndJoin()
                            }
                        }
                    }
                    messageDisplay.showMessage(R.string.network_not_found)
                }
            }
            networkMonitor.broadcastIP.observeForever(observer)
            if (networkMonitor.broadcastIP.value == null) {
                messageDisplay.showMessage(R.string.network_not_found)
            } else {
                messageDisplay.showMessage(R.string.network_found)
            }
        }
    }

    @ObsoleteCoroutinesApi
    fun stop() {
        isStarted = false
        networkMonitor.stop()
        udpCoroutineContext.cancel()
    }

}