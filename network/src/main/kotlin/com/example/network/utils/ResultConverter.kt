package com.example.network.utils

import retrofit2.Call
import retrofit2.awaitResponse
import com.example.common.Result

@Suppress("UNCHECKED_CAST")
suspend fun <R> Call<R>.toResult(): Result<R?> = runCatchingFromSuspend {
    val response = awaitResponse()
    when {
        response.isSuccessful -> successOf(response.body())
        else -> Result.Error(message = response.message() ?: "")
    }
}
    .fold(
        onSuccess = { it },
        onFailure = { throwable -> Result.Error(throwable.message, throwable) }
    )