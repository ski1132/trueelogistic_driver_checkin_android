package com.example.testwithfirebase

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.tbouron.shakedetector.library.ShakeDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btScanQR.setOnClickListener {
            activity?.let { fragActivity ->
                KotlinPermissions.with(fragActivity) // where this is an FragmentActivity instance
                    .permissions(
                        Manifest.permission.CAMERA
                    ).onAccepted {
                        fragActivity.supportFragmentManager.beginTransaction()
                            .replace(R.id.contentMainFrag, ScanQrFragment()).commit()
                    }.onDenied {
                        Toast.makeText(
                            fragActivity, "Permission Denied",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .onForeverDenied {
                        Toast.makeText(
                            fragActivity, " Forever Denied",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .ask()
            }
        }
        btSentNearBy.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.contentMainFrag, NearByFragment())
                ?.addToBackStack(null)?.commit()
        }
        btShake.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.contentMainFrag, ShakeFragment())
                ?.addToBackStack(null)?.commit()
        }
    }
}