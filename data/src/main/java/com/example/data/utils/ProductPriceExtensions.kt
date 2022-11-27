package com.example.data.utils

import com.example.data.api.ProductPrice
import java.util.*

fun ProductPrice?.getFormattedPrice(): String {
    this?.let {
        val currency = Currency.getInstance(this.currency)
        val currencySymbol = currency.getSymbol(Locale.getDefault())
        val formattedAmount = String.format("%.${this.decimals}f", this.amount.toFloat())

        return "$formattedAmount$currencySymbol"
    } ?: return ""
}