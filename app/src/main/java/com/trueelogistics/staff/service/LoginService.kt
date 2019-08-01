package com.trueelogistics.staff.service

import com.trueelogistics.staff.model.ProfileRootModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService{
    @POST("check-in/v1/auth/login")
    @FormUrlEncoded
    fun getData(
        @Field("username") username:String,
        @Field("password")password:String
    ) : Call<ProfileRootModel>
}