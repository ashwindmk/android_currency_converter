package com.ashwin.android.currencyconverter.main

import com.ashwin.android.currencyconverter.data.network.CurrencyApi
import com.ashwin.android.currencyconverter.model.CurrencyResponse
import com.ashwin.android.currencyconverter.util.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(private val api: CurrencyApi) : MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

}
