package com.example.data.api

import com.example.network.ApiProvider
import com.example.network.utils.toResult

class BuxRestApiClient(
    private val apiProvider: ApiProvider<BuxApi>
) {

    suspend fun getProducts() = apiProvider.api().getProducts().toResult()

    suspend fun getProductDetails(productId: String) = apiProvider.api().getProductDetails(productId).toResult()

}