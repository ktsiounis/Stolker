package com.example.network.utils

import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
class BuxWebSocketListener : WebSocketListener() {

    val socketEventChannel: Channel<SocketUpdate> = Channel(10)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("BuxWebSocketListener", response.message)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {
            Log.d("BuxWebSocketListener", text)
            if (!socketEventChannel.isClosedForSend) {
                socketEventChannel.send(SocketUpdate(text))
            }
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("BuxWebSocketListener", "$code - $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch {
            Log.d("BuxWebSocketListener", "Socket failure: ${t.message} - ${response?.message}")
            if (!socketEventChannel.isClosedForSend) {
                socketEventChannel.send(SocketUpdate(text = response?.message))
            }
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("BuxWebSocketListener", "Socket Closed")
    }

}