package com.example.network.utils

import com.example.common.Result

fun <T> successOf(value: T) = Result.Success(value)