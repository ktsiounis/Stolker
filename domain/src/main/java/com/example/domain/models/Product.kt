package com.example.domain.models

import java.io.Serializable

data class Product(
    val name: String,
    val id: String,
    val currentPrice: String,
    val closingPrice: String
) : Serializable
