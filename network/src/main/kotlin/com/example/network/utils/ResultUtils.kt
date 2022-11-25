package com.example.network.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException

inline fun <T> runCatchingFromSuspend(block: () -> T): Result<T> {
    return runCatching { block() }
        .onFailure { if (it is CancellationException && it !is TimeoutCancellationException) throw it }
}