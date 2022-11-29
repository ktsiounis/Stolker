package com.example.domain.usecases

import com.example.domain.contracts.BuxRestRepositoryContract
import com.example.domain.contracts.BuxWebSocketRepositoryContract
import kotlinx.coroutines.CoroutineScope

class ProductDetailsUseCase(
    private val restRepository: BuxRestRepositoryContract,
    private val webSocketRepository: BuxWebSocketRepositoryContract
) {

    suspend fun getProducts() = restRepository.getProducts()

    suspend fun getProductDetails(id: String) = restRepository.getProductDetails(id)

    suspend fun startSocket(scope: CoroutineScope) = webSocketRepository.startSocket(scope)

    fun closeSocket() = webSocketRepository.closeSocket()

    fun subscribeTo(productId: String) = webSocketRepository.subscribeTo(productId)

    fun unsubscribeFrom(productId: String) = webSocketRepository.unsubscribeFrom(productId)

}