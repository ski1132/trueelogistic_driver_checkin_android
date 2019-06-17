package com.example.checklibrary.service

import com.example.checklibrary.model.RootModel
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Header

interface QrService{
    @POST("/check-in/v1/qrcode/create")
    @FormUrlEncoded
    fun getData(
        @Field("qrcodeCreateBy") qrcodeCreateBy:String,
        @Field("locationId")locationId:String) : Call<RootModel>

}