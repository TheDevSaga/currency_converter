package com.example.currencyconverter.main

import android.util.Log
import com.example.currencyconverter.data.CurrencyApi
import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.utils.Resource
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: CurrencyApi
):MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if(response.isSuccessful && result!=null){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        }catch (e:java.lang.Exception){
            Log.e("CurrencyAPI", "getRates: ${e.message}", )
            return Resource.Error(e.message?:"Something Went Wrong")
        }
    }
}