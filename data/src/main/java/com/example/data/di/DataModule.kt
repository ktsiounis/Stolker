package com.example.data.di

import com.example.data.api.BuxApi
import com.example.data.api.BuxRestApiClient
import com.example.data.api.BuxRestRepository
import com.example.data.socket.BuxWebSocketRepository
import com.example.domain.contracts.BuxRestRepositoryContract
import com.example.domain.contracts.BuxWebSocketRepositoryContract
import com.example.network.ApiProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {

    factory { BuxRestApiClient(apiProvider = ApiProvider(BuxApi::class.java)) }
    factoryOf(::BuxRestRepository) bind BuxRestRepositoryContract::class
    factoryOf(::BuxWebSocketRepository) bind BuxWebSocketRepositoryContract::class

}