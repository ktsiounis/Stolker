package com.example.stolker.ui.viewmodels

import android.util.Log
import app.cash.turbine.test
import com.example.common.Result
import com.example.data.api.BuxProductRaw
import com.example.data.api.ProductPrice
import com.example.domain.models.Product
import com.example.domain.models.SocketMessage
import com.example.domain.usecases.ProductDetailsUseCase
import com.example.quotes.runTestWithDispatcher
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {

    private val useCaseMock = mockk<ProductDetailsUseCase>()
    private val vmUnderTest = MainViewModel(useCaseMock)

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
    fun `when products received successfully, ui state with success and products is emitted`() =
        runTestWithDispatcher {
            vmUnderTest.uiState.test {
                coEvery { useCaseMock.getProducts() } returns flowOf(Result.Success(listOf(product)))

                vmUnderTest.getProducts()

                skipItems(1)
                awaitItem() shouldBe ProductsUiState.Success(listOf(product))
                expectNoEvents()
            }
        }

    @Test
    fun `when products fail to be received, ui state with not available products message is emitted`() =
        runTestWithDispatcher {
            vmUnderTest.uiState.test {
                coEvery { useCaseMock.getProducts() } returns flowOf(Result.Error(errorMessage))

                vmUnderTest.getProducts()

                skipItems(1)
                awaitItem() shouldBe ProductsUiState.NotAvailableProducts
                expectNoEvents()
            }
        }

    @Test
    fun `when product details received successfully, ui action to navigate to product is emitted`() =
        runTestWithDispatcher {
            vmUnderTest.uiAction.test {
                coEvery { useCaseMock.getProductDetails(product.id) } returns flowOf(Result.Success(product))

                vmUnderTest.getProductDetails(product.id)

                awaitItem() shouldBe ProductsUiAction.NavigateToProductDetails(product)
                expectNoEvents()
            }
        }

    @Test
    fun `when product details fail to be received, ui state with error message is emitted`() =
        runTestWithDispatcher {
            vmUnderTest.uiState.test {
                coEvery { useCaseMock.getProductDetails(product.id) } returns flowOf(Result.Error(errorMessage))

                vmUnderTest.getProductDetails(product.id)

                skipItems(1)
                awaitItem() shouldBe ProductsUiState.Error(errorMessage)
                expectNoEvents()
            }
        }

    @Test
    fun `when close socket for a product is invoked, useCase unsubscribes from the product and closes socket`() {
        coEvery { useCaseMock.unsubscribeFrom(product.id) } just runs
        coEvery { useCaseMock.closeSocket() } just runs

        vmUnderTest.closeSocketForProduct(product.id)

        verify(exactly = 1) { useCaseMock.unsubscribeFrom(product.id) }
        verify(exactly = 1) { useCaseMock.closeSocket() }
    }

    @Test
    fun `when start socket for a product, and socket is connected successfully, useCase subscribes to product updates`() =
        runTestWithDispatcher {
            mockkStatic(Log::class)
            coEvery { useCaseMock.startSocket() } returns flowOf(SocketMessage.SocketConnected)
            every { Log.d(any(), any()) } returns 0

            vmUnderTest.startSocketForProduct(product.id)

            verify(exactly = 1) { useCaseMock.subscribeTo(product.id) }
        }

    @Test
    fun `when start socket for a product, and socket update is received, details ui state with update is emitted`() =
        runTestWithDispatcher {
            vmUnderTest.detailsUiState.test {
                mockkStatic(Log::class)
                coEvery { useCaseMock.startSocket() } returns flowOf(SocketMessage.ProductUpdate(product.id, "2"))
                every { Log.d(any(), any()) } returns 0

                vmUnderTest.startSocketForProduct(product.id)

                skipItems(1)
                awaitItem() shouldBe ProductDetailsUiState.ProductUpdate("2")
                expectNoEvents()
            }
        }

}