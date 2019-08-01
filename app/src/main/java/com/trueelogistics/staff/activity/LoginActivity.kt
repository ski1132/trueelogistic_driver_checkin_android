package com.trueelogistics.staff.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import com.trueelogistics.staff.R
import com.trueelogistics.staff.model.ProfileRootModel
import com.trueelogistics.staff.service.LoginService
import com.trueelogistics.staff.service.RetrofitGenerater
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        confirmLogin.setOnClickListener {
            getRetrofit(username_input_layout.text.toString(),password_input_layout.text.toString())
        }
    }

    private fun getRetrofit(username : String , password : String){
        val retrofit = RetrofitGenerater().build(true).create(LoginService::class.java)
        val call = retrofit?.getData(username, password)
        call?.enqueue(object : Callback<ProfileRootModel> {
            override fun onFailure(call: Call<ProfileRootModel>, t: Throwable) {

            }
            override fun onResponse(call: Call<ProfileRootModel>, response: Response<ProfileRootModel>) {
                if (response.code() == 200)
                {

                    val logModel : ProfileRootModel ?= response.body()
                    logModel?.data?.let{
                        if (it.role == "Admin"){
                            Hawk.put("TOKEN",it.token)
                            Hawk.put("RETOKEN",it.reToken)
                            Toast.makeText(this@LoginActivity, "Correct Username and Password", Toast.LENGTH_SHORT).show()
                            successLogin()
                        }
                    }
                }
                else {
                    Toast.makeText(this@LoginActivity, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                    response.errorBody()
                }
            }

        })
    }

    private fun successLogin(){
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

