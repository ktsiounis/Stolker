package com.example.domain.models

sealed class SocketMessage{
    data class ProductUpdate(val securityId: String?, val currentPrice: String?): SocketMessage()
    object SocketConnected: SocketMessage()
    data class SocketConnectionFailed(val message: String?, val errorCode: String?): SocketMessage()
    object Unknown: SocketMessage()
}
