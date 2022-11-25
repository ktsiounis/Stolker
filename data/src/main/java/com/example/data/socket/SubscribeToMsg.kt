package com.example.data.socket

import com.google.gson.annotations.SerializedName

data class SubscribeToMsg(
    @SerializedName("subscribeTo") val subscribeTo: List<String>
)
