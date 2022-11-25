package com.example.stolker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Result
import com.example.domain.models.Product
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
                        is Result.Error -> _uiState.value = ProductsUiState.Error(it?.message ?: generalErrorMessage)
                    }
                }
        }
    }

}

sealed interface ProductsUiState {
    data class Success(val products: List<Product>): ProductsUiState
    data class Error(val message: String): ProductsUiState
}