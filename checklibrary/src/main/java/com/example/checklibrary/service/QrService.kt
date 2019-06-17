package com.example.checklibrary.service

import com.example.checklibrary.model.RootModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.POST

interface QrService{
    @POST("/api.staging.sendit.asia/check-in/v1/qrcode/create")
    fun getData(@Field("qrcodeCreateBy") qrcodeCreateBy:String,
                @Field("locationIdModel")locationId:String) : Call<RootModel>

}