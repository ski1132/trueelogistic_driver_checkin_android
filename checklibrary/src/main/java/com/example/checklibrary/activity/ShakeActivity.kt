package com.example.checklibrary.activity

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.checklibrary.R
import com.github.tbouron.shakedetector.library.ShakeDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlinpermissions.KotlinPermissions

@SuppressLint("Registered")
class ShakeActivity : AppCompatActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)

        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)
        KotlinPermissions.with(this) // where this is an FragmentActivity instance --> KotlinPermissions.with
            .permissions(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).onAccepted {
                val fusedLocationClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                ShakeDetector.start()
                ShakeDetector.create(this) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            val latitude = location?.latitude.toString()
                            val longitude = location?.longitude.toString()

                            FirebaseApp.initializeApp(this)

                            val ref= FirebaseDatabase.getInstance().getReference("driverLocation")
                            ref.addValueEventListener( object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    ref.child("latitude").setValue(latitude)
                                    ref.child("longitude").setValue(longitude)
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        this@ShakeActivity,
                                        " Fail to set database !!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                            location?.let{
                                getManagerFirebase(it)
                            }
                        }
                    object : CountDownTimer(5000, 1000) { // 1 second to onTick & 1 minit to onFinish

                        override fun onTick(millisUntilFinished: Long) {

                        }
                        override fun onFinish() {

                        }
                    }.start()
                }
                ShakeDetector.destroy()
            }.onDenied {
                Toast.makeText(
                this,
                " Allow GPS Permission to use !!!",
                Toast.LENGTH_SHORT
                ).show()
            }.ask()

    }
    private fun getManagerFirebase(location: Location)
    {
        val managerRef= FirebaseDatabase.getInstance().getReference("managerLocation")
        managerRef.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val latDriver  = dataSnapshot.child("latitude").value.toString()
                val longDriver = dataSnapshot.child("longitude").value.toString()
                val managerLocation = Location(LocationManager.GPS_PROVIDER)
                managerLocation.latitude = latDriver.toDouble()
                managerLocation.longitude = longDriver.toDouble()
                val distance : Float? = location.distanceTo(managerLocation)
                    if (distance != null) {
                        if (distance < 500)
                            Toast.makeText(
                                this@ShakeActivity,
                                " distance so close == $distance",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            Toast.makeText(
                                this@ShakeActivity,
                                " out of range == $distance",
                                Toast.LENGTH_SHORT
                            ).show()
                    }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ShakeActivity,
                    " Fail to get database !!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    fun getDriverFirebase(location: Location)
    {
        val managerRef= FirebaseDatabase.getInstance().getReference("driverLocation")
        managerRef.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val latDriver  = dataSnapshot.child("latitude").value.toString()
                val longDriver = dataSnapshot.child("longitude").value.toString()
                val driverLocation = Location(LocationManager.GPS_PROVIDER)
                driverLocation.latitude = latDriver.toDouble()
                driverLocation.longitude = longDriver.toDouble()
                val distance : Float? = location.distanceTo(driverLocation)
                    if (distance != null) {
                        if (distance < 500)
                            Toast.makeText(
                                this@ShakeActivity,
                                " distance so close == $distance",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            Toast.makeText(
                                this@ShakeActivity,
                                " out of range == $distance",
                                Toast.LENGTH_SHORT
                            ).show()
                    }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ShakeActivity,
                    " Fail to get database !!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
