package com.example.common

sealed class Result<out T> {

    data class Error(val message: String?, val throwable: Throwable? = null) : Result<Nothing>()

    data class Success<T>(val value: T) : Result<T>()

    override fun toString(): String = when (this) {
        is Success -> "Success[$value]"
        is Error -> "Error[${super.toString()}]"
    }

}