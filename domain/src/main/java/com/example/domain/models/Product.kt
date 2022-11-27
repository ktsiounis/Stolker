package com.example.domain.models

import java.io.Serializable

data class Product(
    val name: String,
    val id: String,
    val currentPrice: Float,
    val closingPrice: Float,
    val currentPriceFormatted: String,
    val closingPriceFormatted: String
) : Serializable
