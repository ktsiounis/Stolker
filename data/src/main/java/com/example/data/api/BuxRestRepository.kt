package com.example.data.api

import com.example.domain.contracts.BuxRestRepositoryContract
import com.example.domain.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.example.common.Result

class BuxRestRepository(
    private val client: BuxRestApiClient
) : BuxRestRepositoryContract {

    override suspend fun getProducts(): Flow<Result<List<Product>>> = flowOf(
        client.getProducts().toProductsResult()
    )

    override suspend fun getProductDetails(id: String): Flow<Result<Product>> = flowOf(
        client.getProductDetails(id).toProductResult()
    )

}