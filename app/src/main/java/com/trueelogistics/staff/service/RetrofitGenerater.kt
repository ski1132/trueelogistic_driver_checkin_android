package com.trueelogistics.staff.service

import com.orhanobut.hawk.Hawk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitGenerater {
    fun build(isRequestHeader: Boolean? = false) : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                if(isRequestHeader == true) {
                    newRequest.addHeader("Content-Type", "application/json")
                        .addHeader("Token", Hawk.get("TOKEN")?:"")
                        .addHeader("ReToken", Hawk.get("RETOKEN")?:"")
                }
                else{
                    newRequest.addHeader("Authorization", "")
                }
                chain.proceed(newRequest.build())
            }
            .build()
        return Retrofit.Builder().baseUrl("http://api.staging.sendit.asia")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}