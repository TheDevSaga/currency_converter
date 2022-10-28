package com.example.currencyconverter.di

import com.example.currencyconverter.constans.Constants
import com.example.currencyconverter.data.CurrencyApi
import com.example.currencyconverter.main.MainRepository
import com.example.currencyconverter.main.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(Constants.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor(

                ).setLevel(HttpLoggingInterceptor.Level.BODY)).build()
        )
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = MainRepositoryImpl(api)
}