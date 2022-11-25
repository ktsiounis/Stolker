package com.example.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BuxApi {

    companion object {
        private const val PRODUCTS_LIST = "core/27/products"
        private const val PRODUCT_DETAILS = "core/27/products/{productId}"
    }

    @GET(PRODUCTS_LIST)
    fun getProducts(): Call<List<BuxProductRaw>>

    @GET(PRODUCT_DETAILS)
    fun getProductDetails(@Path("productId") productId: String): Call<BuxProductRaw>

}