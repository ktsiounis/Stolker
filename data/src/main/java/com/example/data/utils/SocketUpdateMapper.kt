package com.example.data.utils

import com.example.data.socket.SocketMessageRaw
import com.example.data.socket.SocketUpdateType
import com.example.domain.models.SocketMessage

fun SocketMessageRaw.toSocketMessage(): SocketMessage {
    return when(this.type) {
        SocketUpdateType.PRODUCT -> {
            SocketMessage.ProductUpdate(this.body?.securityId, this.body?.currentPrice)
        }
        SocketUpdateType.SUCCESS_CONNECT -> SocketMessage.SocketConnected
        SocketUpdateType.FAIL_CONNECT -> {
            SocketMessage.SocketConnectionFailed(this.body?.developerMessage, this.body?.errorCode)
        }
        else -> SocketMessage.Unknown
    }
}