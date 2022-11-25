package com.example.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class ApiProvider<API>(
    private val apiContract: Class<API>
) {

    companion object {
        const val BASE_URL = BuildConfig.apiBaseUrl
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor {
            val newRequest = it.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + BuildConfig.betaApiAuthToken)
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "nl-NL,en;q=0.8")
                .build()
            return@addInterceptor it.proceed(newRequest)
        }
        .addInterceptor(HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    private fun getRetrofit(): Retrofit = with(Retrofit.Builder()) {
        baseUrl(BASE_URL)
        client(client)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    fun api(): API {
        return getRetrofit().create(apiContract)
    }

}