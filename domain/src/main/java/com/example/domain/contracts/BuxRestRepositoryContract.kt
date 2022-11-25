package com.example.domain.contracts

import com.example.domain.models.Product
import kotlinx.coroutines.flow.Flow
import com.example.common.Result

interface BuxRestRepositoryContract {

    suspend fun getProducts(): Flow<Result<List<Product>>>

    suspend fun getProductDetails(id: String): Flow<Result<Product>>

}