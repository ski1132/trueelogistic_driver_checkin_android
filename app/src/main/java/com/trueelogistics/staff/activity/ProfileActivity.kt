package com.trueelogistics.staff.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.trueelogistics.staff.R
import com.trueelogistics.staff.fragment.ProfileShowFragment
import com.trueelogistics.staff.model.ProfileRootModel
import com.trueelogistics.staff.service.ProfileService
import com.trueelogistics.staff.service.RetrofitGenerater
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_profile, ProfileShowFragment())
            .commit()
    }

    override fun onBackPressed() {
        if (this.supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else
            super.onBackPressed()
    }

    fun getProfileData(listenner: ProfileDataCallback) {
        val retrofit = RetrofitGenerater().build(true).create(ProfileService::class.java)
        val call = retrofit.getData()
        call.enqueue(object : Callback<ProfileRootModel> {
            override fun onFailure(call: Call<ProfileRootModel>, t: Throwable) {
                listenner.onFailureProfile(t.message ?: "ProfileData.onFailure ")
            }

            override fun onResponse(
                call: Call<ProfileRootModel>,
                response: Response<ProfileRootModel>
            ) {
                when (response.code()) {
                    200 -> {
                        listenner.onResponceProfile(response.body())

                    }
                    else -> {
                        listenner.onFailureProfile(response.message())
                    }
                }

            }
        })
    }

    interface ProfileDataCallback {
        fun onResponceProfile(model: ProfileRootModel?)
        fun onFailureProfile(message: String)
    }
}
