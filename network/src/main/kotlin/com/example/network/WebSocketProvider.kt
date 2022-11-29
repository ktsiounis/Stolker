package com.example.network

import com.example.network.utils.BuxWebSocketListener
import com.example.network.utils.SocketUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor

open class WebSocketProvider {

    private var _webSocket: WebSocket? = null
    private var _webSocketListener: BuxWebSocketListener? = null

    companion object {
        const val BASE_URL = BuildConfig.rtfBaseUrl
        const val NORMAL_CLOSURE_STATUS = 1000
    }

    private val client = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .addInterceptor {
            val newRequest = it.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + BuildConfig.rtfAuthToken)
                .addHeader("Accept-Language", "nl-NL,en;q=0.8")
                .build()
            return@addInterceptor it.proceed(newRequest)
        }
        .addInterceptor(HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    fun startSocket(scope: CoroutineScope): SharedFlow<SocketUpdate> =
        if (_webSocket == null) {
            with(BuxWebSocketListener(scope)) {
                startSocket(this)
                this@with.socketEventFlow
            }
        } else _webSocketListener!!.socketEventFlow

    private fun startSocket(webSocketListener: BuxWebSocketListener) {
        _webSocketListener = webSocketListener
        _webSocket = client.newWebSocket(
            Request.Builder().url(BASE_URL).build(),
            webSocketListener
        )
    }

    fun stopSocket() {
        try {
            _webSocket?.close(NORMAL_CLOSURE_STATUS, null)
            _webSocket = null
            _webSocketListener = null
        } catch (ex: Exception) {
        }
    }

    fun subscribeTo(message: String) {
        _webSocket?.send(message)
    }

    fun unsubscribeFrom(message: String) {
        _webSocket?.send(message)
    }

}