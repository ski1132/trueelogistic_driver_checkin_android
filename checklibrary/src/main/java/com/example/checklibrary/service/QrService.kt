package com.example.checklibrary.service

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface QrService{
    @POST("/api.staging.sendit.asia/check-in/v1/qrcode/create")
    fun getData(@Query("token") token:String) : Call<String>

}