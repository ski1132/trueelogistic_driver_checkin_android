package com.trueelogistics.staff.service

import com.trueelogistics.staff.model.ProfileRootModel
import retrofit2.Call
import retrofit2.http.*

interface ProfileEditService {
    @FormUrlEncoded
    @PUT("check-in/v1/admin/users/{id}")
    fun getData(
        @Path("id") id: String ?= "",
        @Field("imgProfile") imgProfile: String,
        @Field("firstname") firstname: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<ProfileRootModel>
}