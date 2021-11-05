package com.ashwin.android.currencyconverter.model

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Rates
)
