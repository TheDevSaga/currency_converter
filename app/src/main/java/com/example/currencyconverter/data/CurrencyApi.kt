package com.example.currencyconverter.data

import com.example.currencyconverter.constans.Constants
import com.example.currencyconverter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/exchangerates_data/latest")
    suspend fun getRates(
        @Query("base") base: String,
        @Query("apikey") apiKey: String = Constants.apiKey
    ): Response<CurrencyResponse>
}