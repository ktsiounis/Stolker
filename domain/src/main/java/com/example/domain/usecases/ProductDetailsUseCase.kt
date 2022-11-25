package com.example.domain.usecases

import com.example.domain.contracts.BuxRestRepositoryContract
import com.example.domain.contracts.BuxWebSocketRepositoryContract

class ProductDetailsUseCase(
    private val restRepository: BuxRestRepositoryContract,
    private val webSocketRepository: BuxWebSocketRepositoryContract
) {

    suspend fun getProducts() = restRepository.getProducts()

    suspend fun getProductDetails(id: String) = restRepository.getProductDetails(id)

}