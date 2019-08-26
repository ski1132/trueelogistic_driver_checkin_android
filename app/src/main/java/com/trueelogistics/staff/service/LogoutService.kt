package com.trueelogistics.staff.service

import com.trueelogistics.staff.model.LoginRootModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LogoutService{
    @POST("check-in/v1/auth/revoke")
    @FormUrlEncoded
    fun getData(
        @Field("username") username:String,
        @Field("latitude") latitude : String,
        @Field("longitude") longitude : String
    ) : Call<LoginRootModel>
}
