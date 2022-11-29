package com.example.network.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class BuxWebSocketListener(private val scope: CoroutineScope) : WebSocketListener() {

    private val _socketEventFlow = MutableSharedFlow<SocketUpdate>()
    val socketEventFlow: SharedFlow<SocketUpdate> = _socketEventFlow.asSharedFlow()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("BuxWebSocketListener", response.message)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        scope.launch {
            Log.d("BuxWebSocketListener", text)
            _socketEventFlow.emit(SocketUpdate(text))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("BuxWebSocketListener", "$code - $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        scope.launch {
            Log.d("BuxWebSocketListener", "Socket failure: ${t.message} - ${response?.message}")
            _socketEventFlow.emit(SocketUpdate(text = response?.message))
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("BuxWebSocketListener", "Socket Closed")
    }

}