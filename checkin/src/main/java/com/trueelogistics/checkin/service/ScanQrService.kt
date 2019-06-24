package com.trueelogistics.checkin.service

import com.trueelogistics.checkin.model.generate_qr.RootModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ScanQrService{
    @POST("/check-in/v1/history/create")
    @FormUrlEncoded
    fun getData(
        @Field("type") type:String,
        @Field("qrcodeUniqueKey")qrcodeUniqueKey:String) : Call<RootModel>
}