package com.example.data.api

import com.example.common.Result
import com.example.data.utils.getFormattedPrice
import com.example.domain.models.Product

fun Result<BuxProductRaw?>.toProductResult(): Result<Product> {
    return when(this) {
        is Result.Success -> {
            with(this.value) {
                Result.Success(
                    Product(
                        name = this?.displayName ?: "",
                        id = this?.securityId ?: "",
                        currentPrice = this?.currentPrice?.amount?.toFloat() ?: 0f,
                        closingPrice = this?.closingPrice?.amount?.toFloat() ?: 0f,
                        currentPriceFormatted = this?.currentPrice?.getFormattedPrice() ?: "",
                        closingPriceFormatted = this?.closingPrice?.getFormattedPrice() ?: ""
                    )
                )
            }
        }
        is Result.Error -> this
    }
}

fun Result<List<BuxProductRaw>?>.toProductsResult(): Result<List<Product>> {
    return when(this) {
        is Result.Success -> {
            val productsList = this.value?.let {
                it.map { raw ->
                    Product(
                        name = raw.displayName,
                        id = raw.securityId,
                        currentPrice = raw.currentPrice.amount.toFloat(),
                        closingPrice = raw.closingPrice.amount.toFloat(),
                        currentPriceFormatted = raw.currentPrice.getFormattedPrice(),
                        closingPriceFormatted = raw.closingPrice.getFormattedPrice()
                    )
                }
            } ?: emptyList()
            Result.Success(productsList)
        }
        is Result.Error -> this
    }
}