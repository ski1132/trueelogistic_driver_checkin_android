package com.trueelogistics.staff.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.orhanobut.hawk.Hawk
import com.trueelogistics.checkin.fragment.MockDialogFragment
import com.trueelogistics.checkin.handler.CheckInTEL
import com.trueelogistics.staff.R
import com.trueelogistics.staff.model.LoginRootModel
import com.trueelogistics.staff.model.ProfileRootModel
import com.trueelogistics.staff.service.LoginService
import com.trueelogistics.staff.service.ProfileService
import com.trueelogistics.staff.service.RetrofitGenerater
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkToken()
        confirmLogin.setOnClickListener {
            getRetrofit(username_input_layout.text.toString(),password_input_layout.text.toString())
        }
    }

    private fun getRetrofit(username : String , password : String){
        val loadingDialog = ProgressDialog.show(this, "Checking Qr code", "please wait...", true, false)
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        var latitude: Double
        var longitude: Double
        if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation
                    ?.addOnSuccessListener { location: Location? ->
                        if (location?.isFromMockProvider == false) {
                            latitude = location.latitude
                            longitude = location.longitude
                            val retrofit = RetrofitGenerater().build(false).create(LoginService::class.java)
                            val call = retrofit.getData(username, password,latitude.toString(),longitude.toString())
                            call.enqueue(object : Callback<LoginRootModel> {
                                override fun onFailure(call: Call<LoginRootModel>, t: Throwable) {
                                    Toast.makeText(this@LoginActivity, "Fail to Login , ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                                override fun onResponse(call: Call<LoginRootModel>, response: Response<LoginRootModel>) {
                                    when {
                                        response.code() == 200 -> {
                                            val logModel : LoginRootModel ?= response.body()
                                            logModel?.data?.let{
                                                if (it.role == "DRIVER"){
                                                    Hawk.put("TOKEN",it.token)
                                                    Hawk.put("RETOKEN",it.reToken)
                                                    successLogin()
                                                } else{
                                                    val wrongRole = "คุณไม่ใช่ Driver"
                                                    wrong_login.text = wrongRole
                                                }
                                            }
                                        }
                                        response.code() == 404 -> {
                                            val wrongRole = "ชื่อผู้ใช้ หรือรหัสผ่านไม่ถูกต้อง"
                                            wrong_login.text = wrongRole
                                        }
                                        response.code() == 500 ->{
                                            val wrongRole = "กรุณากรอกชื่อผู้ใช้ และรหัสผ่านให้ครบ"
                                            wrong_login.text = wrongRole
                                        }
                                        else -> {
                                            wrong_login.text = response.errorBody().toString()
                                            Toast.makeText(this@LoginActivity, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                                            response.errorBody()
                                        }
                                    }
                                }
                            })

                        } else {
                            loadingDialog?.dismiss()
                            MockDialogFragment().show(supportFragmentManager, "show")
                            val intent = Intent(this, CheckInTEL::class.java)
                            intent.putExtras(
                                Bundle().apply {
                                    putString("error", "GPS is Mock !!")
                                }
                            )
                        }
                    }
            }
            else{
                val intent = Intent(this, CheckInTEL::class.java)
                intent.putExtras(
                    Bundle().apply {
                        putString("error", "Permission GPS Denied!!")
                    }
                )
            }
    }

    private fun checkToken(){
        if (Hawk.get<String>("TOKEN") != null){
            successLogin()
        }
    }

    fun successLogin(){
        val retrofit = RetrofitGenerater().build(true).create(ProfileService::class.java)
        val call = retrofit.getData()
        call.enqueue(object : Callback<ProfileRootModel> {
            override fun onFailure(call: Call<ProfileRootModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Fail to get Profile data , ${t.message}", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<ProfileRootModel>, response: Response<ProfileRootModel>) {
                when( response.code() ){
                    200 -> {
                        val model : ProfileRootModel? = response.body()
                        CheckInTEL.userId = model?.data?.citizenId
                        val firstName = model?.data?.firstName?:""
                        val lastName = model?.data?.lastName?:""
                        Hawk.put("NAME", "$firstName $lastName")
                        Hawk.put("USERNAME", model?.data?.username)
                        finish()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("IMG_SRC",model?.data?.imgProfile)
                        startActivity(intent)
                    }
                    else -> {
                        Log.d(" Responce.Code == ",response.code().toString())
                    }
                }

            }
        })
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Closing Application")
            .setMessage("Are you sure you want to close ?")
            .setPositiveButton("Yes") { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()
    }
}

