package com.example.data.api

import com.example.common.Result
import com.example.domain.models.Product
import com.example.quotes.runTestWithDispatcher
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class BuxRestRepositoryTest{

    private val clientMock = mockk<BuxRestApiClient>()
    private val repoUnderTest = BuxRestRepository(clientMock)

    private val productRaw = BuxProductRaw(
        symbol = "AMZ",
        securityId = "sb22222",
        displayName = "Amazon",
        currentPrice = ProductPrice(
            currency = "USD",
            decimals = 2,
            amount = "200.20"
        ),
        closingPrice = ProductPrice(
            currency = "USD",
            decimals = 2,
            amount = "100.00"
        )
    )

    private val product = Product(
        name = "Amazon",
        id = "sb22222",
        currentPriceFormatted = "200.20US$",
        currentPrice = 200.20f,
        closingPriceFormatted = "100.00US$",
        closingPrice = 100.00f
    )

    private val errorMessage = "Not Found!"

    @Test
    fun `when client returns success with products, repo does the same`() =
        runTestWithDispatcher {
            coEvery { clientMock.getProducts() } returns Result.Success(listOf(productRaw))

            val result = repoUnderTest.getProducts()

            result.collect {
                it shouldBe Result.Success(listOf(product))
            }
        }

    @Test
    fun `when client returns error, repo does the same with a message to display`() =
        runTestWithDispatcher {
            coEvery { clientMock.getProducts() } returns Result.Error(errorMessage, null)

            val result = repoUnderTest.getProducts()

            result.collect {
                it shouldBe Result.Error(errorMessage, null)
            }
        }

    @Test
    fun `when client returns success with the product details, repo does the same`() =
        runTestWithDispatcher {
            coEvery { clientMock.getProductDetails(productRaw.securityId) } returns Result.Success(productRaw)

            val result = repoUnderTest.getProductDetails(product.id)

            result.collect {
                it shouldBe Result.Success(product)
            }
        }

    @Test
    fun `when client returns erroron product details, repo does the same with a message to display`() =
        runTestWithDispatcher {
            coEvery { clientMock.getProducts() } returns Result.Error(errorMessage, null)

            val result = repoUnderTest.getProducts()

            result.collect {
                it shouldBe Result.Error(errorMessage, null)
            }
        }

}