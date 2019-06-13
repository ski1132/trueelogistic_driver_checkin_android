package com.example.checklibrary.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.checklibrary.Interfaces.CheckInTELCallBack
import com.kotlinpermissions.KotlinPermissions
import me.dm7.barcodescanner.zxing.ZXingScannerView

@SuppressLint("Registered")
class ScanQrActivity : AppCompatActivity(){

    private var checkInTELCallBack: CheckInTELCallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBindingView()
        setBindingData()
    }

    private fun setBindingView(){

    }

    private fun setBindingData(){
        KotlinPermissions.with(this) // where this is an FragmentActivity instance
            .permissions(
                Manifest.permission.CAMERA
            ).onAccepted {
                val zXingScannerView = ZXingScannerView(this)
                setContentView(zXingScannerView)
                zXingScannerView.run {
                    startCamera()
                    setResultHandler {
                        stopCamera()

                        val resultString = it.text.toString()
                        Toast.makeText(
                            this@ScanQrActivity, "QR code = $resultString",
                            Toast.LENGTH_LONG
                        ).show()
                        setResult(Activity.RESULT_OK,Intent().apply { // result code and data intent
                            this.putExtra("result",resultString)
                        })
                        finish()
                    }
                }
            }.onDenied {

                Toast.makeText(
                    this, "Permission Denied",
                    Toast.LENGTH_LONG
                ).show()
                checkInTELCallBack?.onCheckInFailure("Permission Denied") // set
                finish()
            }
            .ask()
    }

}
