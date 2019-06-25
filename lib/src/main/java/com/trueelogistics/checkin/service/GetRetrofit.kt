package com.trueelogistics.checkin.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetRetrofit {

    companion object{
        var getRetrofit : GetRetrofit? = null
        fun initial(){
            getRetrofit = GetRetrofit()
        }
    }
    fun build() : Retrofit{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder().baseUrl("http://api.staging.sendit.asia")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}