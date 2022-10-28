package com.example.currencyconverter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.models.Rates
import com.example.currencyconverter.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    sealed class CurrencyEvent {
        class Success(val resultText: String) : CurrencyEvent()
        class Error(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Initial : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Initial)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amt: String,
        from: String,
        to: String
    ) {
        val amount = amt.toFloatOrNull()
        if (amount == null) {
            _conversion.value = CurrencyEvent.Error("Not A Valid Amount")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = CurrencyEvent.Loading
            when (val ratesResponse = repository.getRates(from)) {
                is Resource.Error -> _conversion.value =
                    CurrencyEvent.Error(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(to, rates)
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Error("Unexpected error")
                    } else {
                        val convertedCurrency = (amount * rate * 100).roundToInt() / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$amount $from = $convertedCurrency $to"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates):Double? = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "CHF" -> rates.cHF
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        "MYR" -> rates.mYR
        else -> null
    }
}