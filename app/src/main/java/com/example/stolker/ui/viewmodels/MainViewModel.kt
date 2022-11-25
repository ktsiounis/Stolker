package com.example.stolker.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Result
import com.example.domain.models.Product
import com.example.domain.models.SocketMessage
import com.example.domain.usecases.ProductDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val productDetailsUseCase: ProductDetailsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Success(emptyList()))
    val uiState: StateFlow<ProductsUiState> = _uiState

    private val generalErrorMessage = "Something unexpected happen"

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            productDetailsUseCase
                .getProducts()
                .collect {
                    when(it) {
                        is Result.Success ->_uiState.value = ProductsUiState.Success(it.value)
                        is Result.Error -> _uiState.value = ProductsUiState.Error(it.message ?: generalErrorMessage)
                    }
                }
        }
    }

    fun startSocketForProduct(productId: String) {
        viewModelScope.launch {
            productDetailsUseCase
                .startSocket()
                .collect {
                    when(it) {
                        is SocketMessage.ProductUpdate -> Log.d("MainViewModel", it.currentPrice ?: "")
                        SocketMessage.SocketConnected -> {
                            Log.d("MainViewModel", "Connected!")
                            productDetailsUseCase.subscribeTo(productId)
                        }
                        is SocketMessage.SocketConnectionFailed -> Log.d("MainViewModel", "Connection Failed")
                        SocketMessage.Unknown -> Log.d("MainViewModel", "Unknown message received")
                    }
                }
        }
    }

    fun closeSocketForProduct(productId: String) {
        with(productDetailsUseCase) {
            unsubscribeFrom(productId)
            closeSocket()
        }
    }

}

sealed interface ProductsUiState {
    data class Success(val products: List<Product>): ProductsUiState
    data class Error(val message: String): ProductsUiState
}