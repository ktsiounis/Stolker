package com.example.data.api

data class BuxProductRaw(
    val symbol: String,
    val securityId: String,
    val displayName: String,
    val currentPrice: ProductPrice,
    val closingPrice: ProductPrice,
)

data class ProductPrice(
    val currency: String,
    val decimals: Int,
    val amount: String
)
