package com.example.data.socket

import com.example.data.utils.toTradingProductIdentifier
import com.example.network.WebSocketProvider
import com.example.quotes.runTestWithDispatcher
import com.google.gson.Gson
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class BuxWebSocketRepositoryTest {

    private val providerMock = spyk<WebSocketProvider>()
    private val gsonMock = mockk<Gson>(relaxed = true)
    private val repoUnderTest = BuxWebSocketRepository(providerMock, gsonMock)

    private val productId = "sb22222"

    @Test
    fun `when startSocket is called, provider starts the socket`() = runTestWithDispatcher {
        repoUnderTest.startSocket(this)

        verify(exactly = 1) { providerMock.startSocket(any()) }
    }

    @Test
    fun `when stopSocket is called, provider stops the socket`() = runTestWithDispatcher {
        repoUnderTest.closeSocket()

        verify(exactly = 1) { providerMock.stopSocket() }
    }

    @Test
    fun `when subscribeTo with product is called, provider subscribes to product updates`() =
        runTestWithDispatcher {
            val subscribeMsg = "{\"subscribeTo\": [\"trading.product.{$productId}\"]}"
            every { gsonMock.toJson(SubscribeToMsg(listOf(productId.toTradingProductIdentifier()))) } returns subscribeMsg

            repoUnderTest.subscribeTo(productId)

            verify(exactly = 1) { providerMock.subscribeTo(subscribeMsg) }
        }

    @Test
    fun `when unsubscribeFrom with product is called, provider unsubscribes from product updates`() =
        runTestWithDispatcher {
            val subscribeMsg = "{\"unsubscribeFrom\": [\"trading.product.{$productId}\"]}"
            every { gsonMock.toJson(UnsubscribeFromMsg(listOf(productId.toTradingProductIdentifier()))) } returns subscribeMsg

            repoUnderTest.unsubscribeFrom(productId)

            verify(exactly = 1) { providerMock.unsubscribeFrom(subscribeMsg) }
        }

}