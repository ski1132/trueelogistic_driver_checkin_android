package com.trueelogistics.staff.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.trueelogistics.checkin.fragment.MockDialogFragment
import com.trueelogistics.checkin.handler.CheckInTEL
import com.trueelogistics.checkin.interfaces.ArrayListGenericCallback
import com.trueelogistics.checkin.model.HubInDataModel
import com.trueelogistics.staff.R
import com.trueelogistics.staff.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback {
    override fun onMapReady(googleMap: GoogleMap?) {

        CheckInTEL.checkInTEL?.hubGenerater(object : ArrayListGenericCallback<HubInDataModel> {
            override fun onFailure(message: String?) {
                Toast.makeText(activity, "Generate Hub.onFail : $message ", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(dataModel: ArrayList<HubInDataModel>?) {
                for (i in 0..(dataModel?.size?.minus(1) ?: 0)) {
                    val latitude = dataModel?.get(i)?.latitude
                    val longitude = dataModel?.get(i)?.longitude
                    if (latitude == null && longitude == null) {
                        MockDialogFragment().show(activity?.supportFragmentManager, "show")
//                        MainActivity().onNavigationItemSelected(nav_view.menu.getItem(R.id.scanQr))
                    } else {
                        val work = LatLng(latitude ?: 0.0, longitude ?: 0.0)
                        googleMap?.addMarker(MarkerOptions().position(work).title(dataModel[i].locationName))
                    }

                }
            }
        })
        makerCurrentLocation(googleMap)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        tool_bar.setOnClickListener {
            mainActivity.actionToolbar()
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.show_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun makerCurrentLocation(googleMap: GoogleMap?) {
        activity?.let { activity ->
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            if (ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation
                    ?.addOnSuccessListener { location: Location? ->
                        if (location?.isFromMockProvider == false) {
                            googleMap?.uiSettings?.isZoomControlsEnabled = true
                            googleMap?.isMyLocationEnabled = true
                            val langLong = LatLng(location.latitude, location.longitude)
                            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(langLong, 12f))
                            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(langLong))
                            googleMap?.animateCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.builder()
                                        .target(langLong)
                                        .zoom(15F) //zoom level 0 - 20
                                        .bearing(0F)
                                        .tilt(45F)
                                        .build()
                                )
                            )
                            googleMap?.uiSettings?.isCompassEnabled = false
                        } else {
                            MockDialogFragment().show(activity.supportFragmentManager, "show")
                            activity.supportFragmentManager.beginTransaction()
                                .replace(R.id.frag_main, ScanQrFragment())
                                .commit()
                        }
                    }
            } else {
                Toast.makeText(activity, "Permission denied !!!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
