package com.example.testwithfirebase

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.checklibrary.Interfaces.CheckInTELCallBack
import com.example.checklibrary.handler.CheckInTEL
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btScanQR.setOnClickListener {
            CheckInTEL.checkInTEL?.openScanQRCode(this, object : CheckInTELCallBack {
                override fun onCheckInSuccess(result: String) {
                    Log.e("on ScanQR , Result == ",result)
                }

                override fun onCheckInFailure(message: String) {
                    Log.e("on ScanQR , Message == ",message)
                }

                override fun onCancel() {
                    Log.e("on ScanQR , Cancel == "," == ")
                }

            })
        }
        btSentNearBy.setOnClickListener {
            CheckInTEL.checkInTEL?.openNearBy(this, object : CheckInTELCallBack {
                override fun onCheckInSuccess(result: String) {
                    Log.e("on NearBy , Result == ",result)
                }

                override fun onCheckInFailure(message: String) {
                    Log.e("on NearBy , Message == ",message)
                }

                override fun onCancel() {
                    Log.e("on NearBy , Cancel == "," 000 ")
                }

            })
        }
        btShake.setOnClickListener {
            KotlinPermissions.with(this) // where this is an FragmentActivity instance --> KotlinPermissions.with
                .permissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).onAccepted {
                    CheckInTEL.checkInTEL?.openShake(this, object : CheckInTELCallBack {
                        override fun onCheckInSuccess(result: String) {
                            Log.e("on Shake , Result == ",result)
                        }

                        override fun onCheckInFailure(message: String) {
                            Log.e("on Shake , Message == ",message)
                        }

                        override fun onCancel() {
                            Log.e("on Shake , Cancel == "," 000 ")
                        }

                    })
                }.onDenied {
                    Toast.makeText(
                        this,
                        " Allow GPS Permission to use !!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }.ask()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // ????
        super.onActivityResult(requestCode, resultCode, data)
        CheckInTEL.checkInTEL?.onActivityResult(requestCode, resultCode, data)

    }
}
