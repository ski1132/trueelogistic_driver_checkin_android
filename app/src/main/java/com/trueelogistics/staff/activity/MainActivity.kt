package com.trueelogistics.staff.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.orhanobut.hawk.Hawk
import com.trueelogistics.checkin.fragment.MockDialogFragment
import com.trueelogistics.checkin.handler.CheckInTEL
import com.trueelogistics.staff.R
import com.trueelogistics.staff.fragment.*
import com.trueelogistics.staff.model.LoginRootModel
import com.trueelogistics.staff.service.LogoutService
import com.trueelogistics.staff.service.RetrofitGenerater
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_menu_drawer.*
import kotlinx.android.synthetic.main.activity_main_menu_drawer.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setNavigationItemSelectedListener(this)
        showDataUser()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frag_main, ScanQrFragment())
            .commit()

        nav_view.getHeaderView(0).imageUser.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        system_check_out.setOnClickListener {
            logoutWithLatLong()
        }
    }

    private fun showDataUser(){
        nav_view.getHeaderView(0).nameText.text = Hawk.get("NAME")
        Glide.with(this)
            .load(Hawk.get("IMG_SRC", ""))
            .into(nav_view.getHeaderView(0).imageUser)
    }

    private fun logoutWithLatLong() {
        val loadingDialog =
            ProgressDialog.show(this, "Logout Processing ", " Please wait...", true, false)
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
                            RetrofitGenerater().build(true).create(LogoutService::class.java)
                        val call = retrofit.getData(
                            CheckInTEL.userId.toString(),
                            latitude.toString(),
                            longitude.toString()
                        )
                        call.enqueue(object : Callback<LoginRootModel> {
                            override fun onFailure(call: Call<LoginRootModel>, t: Throwable) {
                                Toast.makeText(
                                    this@MainActivity,
                                    t.message ?: " onFailure",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: Call<LoginRootModel>,
                                response: Response<LoginRootModel>
                            ) {
                                when {
                                    response.code() == 200 -> {
                                        Hawk.deleteAll()
                                        finish()
                                        intent =
                                            Intent(this@MainActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                    }
                                    else -> {
                                        Toast.makeText(
                                            this@MainActivity,
                                            response.message(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        response.errorBody()
                                    }
                                }
                            }
                        })

                    } else {
                        loadingDialog?.dismiss()
                        MockDialogFragment().show(supportFragmentManager, "show")
                    }
                }
        } else {
            Toast.makeText(this, " Permission Location Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 0) {
                AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Application")
                    .setMessage("Are you sure you want to close ?")
                    .setPositiveButton("Yes") { _, _ -> finish() }
                    .setNegativeButton("No", null)
                    .show()
            } else
                super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        nameText.text = Hawk.get("NAME")
        when (item.itemId) {
            R.id.scanQr -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, ScanQrFragment())
                    .commit()
            }
            R.id.shakeFine -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, ShakeFragment())
                    .commit()
            }
            R.id.nearbyFine -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, NearByFragment())
                    .commit()
            }
            R.id.showMap -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, MapFragment())
                    .commit()
            }
            R.id.history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, HistoryFragment())
                    .commit()
            }
//            R.id.absent -> {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.frag_main, AbsentFragment())
//                    .commit()
//            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        showDataUser()
        super.onResume()
    }

    fun actionToolbar() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

}
