package com.ashwin.android.currencyconverter.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashwin.android.currencyconverter.model.Rates
import com.ashwin.android.currencyconverter.util.DispatchersProvider
import com.ashwin.android.currencyconverter.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatchersProvider
) : ViewModel() {
    sealed class CurrencyEvent {
        class Success(val resultText: String) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Initial : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Initial)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(inputAmount: String, fromCurrency: String, toCurrency: String) {
//        Log.d()
        val fromAmount = inputAmount.toFloatOrNull()
        if (fromAmount == null ) {
            _conversion.value = CurrencyEvent.Failure("Not a valid number")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            when (val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(rates = rates, currency = toCurrency)
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unknown currency $toCurrency")
                    } else {
                        val toAmount = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success("$fromAmount $fromCurrency  = $toAmount $toCurrency")
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "PHP" -> rates.pHP
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "RON" -> rates.rON
        "SEK" -> rates.sEK
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "RUB" -> rates.rUB
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "THB" -> rates.tHB
        "CHF" -> rates.cHF
        "SGD" -> rates.sGD
        "PLN" -> rates.pLN
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "ZAR" -> rates.zAR
        "USD" -> rates.uSD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        "MYR" -> rates.mYR
        else -> null
    }
}
