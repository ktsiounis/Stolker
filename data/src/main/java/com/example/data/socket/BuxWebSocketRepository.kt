package com.example.data.socket

import com.example.data.utils.toSocketMessage
import com.example.data.utils.toTradingProductIdentifier
import com.example.domain.contracts.BuxWebSocketRepositoryContract
import com.example.domain.models.SocketMessage
import com.example.network.WebSocketProvider
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map

class BuxWebSocketRepository(
    private val webSocketProvider: WebSocketProvider,
    private val gson: Gson
): BuxWebSocketRepositoryContract {

    override suspend fun startSocket(): Flow<SocketMessage> {
        return webSocketProvider.startSocket().consumeAsFlow().map {
            val messageRaw = gson.fromJson(it.text, SocketMessageRaw::class.java)
            messageRaw.toSocketMessage()
        }
    }

    override fun closeSocket() {
        webSocketProvider.stopSocket()
    }

    override fun subscribeTo(productId: String) {
        val subscription = gson.toJson(SubscribeToMsg(listOf(productId.toTradingProductIdentifier())))
        webSocketProvider.subscribeTo(subscription)
    }

    override fun unsubscribeFrom(productId: String) {
        val unsubscription = gson.toJson(UnsubscribeFromMsg(listOf(productId.toTradingProductIdentifier())))
        webSocketProvider.unsubscribeFrom(unsubscription)
    }

}