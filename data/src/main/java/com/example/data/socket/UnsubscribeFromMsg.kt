package com.example.data.socket

import com.google.gson.annotations.SerializedName

data class UnsubscribeFromMsg(
    @SerializedName("unsubscribeFrom") val unsubscribeFrom: List<String>
)
