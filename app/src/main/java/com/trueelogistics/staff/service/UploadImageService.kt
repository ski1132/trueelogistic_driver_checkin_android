package com.trueelogistics.staff.service

import com.trueelogistics.leader.model.LoadImageDataModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadImageService {
    @Multipart
    @POST("check-in/v1/upload")
    fun getData(
        @Part image: MultipartBody.Part
    ): Call<LoadImageDataModel>
}