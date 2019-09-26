package com.trueelogistics.staff.service

import com.orhanobut.hawk.Hawk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitTokenGenerater {
    fun build(isRequestHeader: Boolean? = true) : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                newRequest.addHeader("Content-Type", "application/json")
                if(isRequestHeader == true) {
                    val token : String = Hawk.get("TOKENPROFILE")
                    newRequest.addHeader("Authorization", token)
                }
                chain.proceed(newRequest.build())
            }
            .build()
        return Retrofit.Builder().baseUrl("http://api.staging.sendit.asia")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}