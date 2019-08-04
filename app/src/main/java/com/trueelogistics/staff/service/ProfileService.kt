package com.trueelogistics.staff.service

import com.trueelogistics.staff.model.ProfileRootModel
import retrofit2.Call
import retrofit2.http.GET

interface ProfileService{
    @GET("check-in/v1/auth/profile")
    fun getData() : Call<ProfileRootModel>
}