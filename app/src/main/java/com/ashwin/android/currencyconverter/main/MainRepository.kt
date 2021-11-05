package com.ashwin.android.currencyconverter.main

import com.ashwin.android.currencyconverter.model.CurrencyResponse
import com.ashwin.android.currencyconverter.util.Resource

interface MainRepository {
    suspend fun getRates(base: String): Resource<CurrencyResponse>
}