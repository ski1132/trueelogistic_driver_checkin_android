package com.example.testwithfirebase


import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.tbouron.shakedetector.library.ShakeDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.fragment_shake.*

class ShakeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shake, container, false)
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { fragActivity ->
            KotlinPermissions.with(fragActivity) // where this is an FragmentActivity instance
                .permissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).onAccepted {
                    val fusedLocationClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(fragActivity)
                    ShakeDetector.start()
                    ShakeDetector.create(fragActivity) {
                        shakeText.text = "Searching..."
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location: Location? ->
                                val latitude = location?.latitude.toString()
                                val longitude = location?.longitude.toString()
                                Log.e(" location.latitude ==", latitude)
                                Log.e(" location.longitude ==", longitude)

                                FirebaseApp.initializeApp(fragActivity)
                                val ref= FirebaseDatabase.getInstance().getReference("location")
                                ref.addValueEventListener( object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                            ref.child("latitude").setValue(latitude)
//                                            ref.child("longitude").setValue(longitude)
                                        val latDriver  = dataSnapshot.child("latitude").value.toString()
                                        val longDriver = dataSnapshot.child("longitude").value.toString()
                                        Log.e(" distance == ",latDriver)
                                        val driveLocation = Location(LocationManager.GPS_PROVIDER)
                                        driveLocation.latitude = latDriver.toDouble()
                                        driveLocation.longitude = longDriver.toDouble()
                                        Log.e(" distance.lat == ",driveLocation.latitude.toString())
                                        val distance : Float = location!!.distanceTo(driveLocation)
                                        Log.e(" distance == ",distance.toString())
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e("=== 0nCeancel ==", "Failed to read value.", error.toException())
                                    }
                                })
                                Toast.makeText(
                                    fragActivity,
                                    "location.longitude = $longitude , latitude = $latitude" ,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        object : CountDownTimer(10000, 1000) { // 1 second to onTick & 1 minit to onFinish

                            override fun onTick(millisUntilFinished: Long) {

                            }
                            override fun onFinish() {
                                ShakeDetector.destroy()
                                shakeText.text = "Can't find anything !! Try Again"
                            }
                        }.start()
                    }


                }.ask()
        }

        Log.e(" you can Shake IT ===", " Now !! ")
    }

}
