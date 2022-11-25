package com.example.data.socket

import com.google.gson.annotations.SerializedName

data class SocketMessageRaw(
    @SerializedName("t")
    val type: SocketUpdateType?,
    @SerializedName("body")
    val body: SocketUpdateBody?
)

enum class SocketUpdateType(val value: String) {
    @SerializedName("trading.quote") PRODUCT("trading.quote"),
    @SerializedName("connect.connected") SUCCESS_CONNECT("connect.connected"),
    @SerializedName("connect.failed") FAIL_CONNECT("connect.failed")
}

data class SocketUpdateBody(
    val securityId: String?,
    val currentPrice: String?,
    val developerMessage: String?,
    val errorCode: String?
)
