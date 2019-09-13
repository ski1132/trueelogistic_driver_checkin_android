package com.trueelogistics.staff.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
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

        checkTokenInHawk()
        confirmLogin.setOnClickListener {
            val name = username_input_layout.text.toString()
            val pass = password_input_layout.text.toString()
            if (username_input_layout.length() == 13 && pass != "")
                getRetrofit(name, pass)
            else if (name.length < 13)
                wrong_login.text = getString(R.string.username_must_equal_13)
            else
                wrong_login.text = getString(R.string.null_input)
        }
    }

    private fun getRetrofit(username: String, password: String) {
        val loadingDialog =
            ProgressDialog.show(
                this, "Login Processing  ", " Please wait..."
                , false, true
            )
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
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
                        val retrofit =
                            RetrofitGenerater().build(false).create(LoginService::class.java)
                        val call = retrofit.getData(
                            username,
                            password,
                            latitude.toString(),
                            longitude.toString()
                        )
                        call.enqueue(object : Callback<LoginRootModel> {
                            override fun onFailure(call: Call<LoginRootModel>, t: Throwable) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Fail to Login , ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                loadingDialog.dismiss()
                            }

                            override fun onResponse(
                                call: Call<LoginRootModel>,
                                response: Response<LoginRootModel>
                            ) {
                                loadingDialog.dismiss()
                                when {
                                    response.code() == 200 -> {
                                        val logModel: LoginRootModel? = response.body()
                                        logModel?.data?.let {
                                            if (it.role == "DRIVER") {
                                                Hawk.put("TOKENPROFILE", it.token)
                                                tokenCheck()
                                            } else {
                                                val wrongRole = getString(R.string.you_not_driver)
                                                wrong_login.text = wrongRole
                                            }
                                        }
                                    }
                                    response.code() == 404 -> {
                                        val wrongRole = getString(R.string.wrong_username)
                                        wrong_login.text = wrongRole
                                    }
                                    else -> {
                                        wrong_login.text = response.message()
                                    }
                                }
                            }
                        })

                    } else {
                        loadingDialog.dismiss()
                        MockDialogFragment().show(supportFragmentManager, "show")
                        val intent = Intent(this, CheckInTEL::class.java)
                        intent.putExtras(
                            Bundle().apply {
                                putString("error", "GPS is Mock !!")
                            }
                        )
                    }
                }
        } else {
            loadingDialog.dismiss()
            val intent = Intent(this, CheckInTEL::class.java)
            intent.putExtras(
                Bundle().apply {
                    putString("error", "Permission GPS Denied!!")
                }
            )
        }
    }

    private fun checkTokenInHawk() {
        val token = Hawk.get<String>("TOKENPROFILE")
        if (Hawk.get<String>("TOKENPROFILE") != null) {
            tokenCheck()
        }
    }

    fun tokenCheck() {
        val loadingDialog =
            ProgressDialog.show(
                this, "Checking Token  ", " Please wait..."
                , false, true
            )
        val retrofit = RetrofitGenerater().build(true).create(ProfileService::class.java)
        val call = retrofit.getData()
        call.enqueue(object : Callback<ProfileRootModel> {
            override fun onFailure(call: Call<ProfileRootModel>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Fail to get Profile data , ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                loadingDialog.dismiss()
            }

            override fun onResponse(
                call: Call<ProfileRootModel>,
                response: Response<ProfileRootModel>
            ) {
                loadingDialog.dismiss()
                when (response.code()) {
                    200 -> {
                        val model: ProfileRootModel? = response.body()
                        CheckInTEL.userId = model?.data?.citizenId
                        finish()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    401 -> {
                        Hawk.deleteAll()
                        val wrongRole = getString(R.string.token_expire)
                        wrong_login.text = wrongRole
                    }
                    else -> {
                        wrong_login.text = response.message()
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

