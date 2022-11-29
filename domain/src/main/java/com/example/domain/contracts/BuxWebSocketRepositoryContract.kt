package com.example.domain.contracts

import com.example.domain.models.SocketMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BuxWebSocketRepositoryContract {

    suspend fun startSocket(scope: CoroutineScope): Flow<SocketMessage>

    fun closeSocket()

    fun subscribeTo(productId: String)

    fun unsubscribeFrom(productId: String)

}