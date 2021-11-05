package com.ashwin.android.currencyconverter.data.network

import com.ashwin.android.currencyconverter.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/latest")
    suspend fun getRates(@Query("base") base: String): Response<CurrencyResponse>
}
