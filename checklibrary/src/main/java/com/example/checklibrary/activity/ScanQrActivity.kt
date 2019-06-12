package com.example.checklibrary.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.checklibrary.R
import com.kotlinpermissions.KotlinPermissions
import me.dm7.barcodescanner.zxing.ZXingScannerView

@SuppressLint("Registered")
class ScanQrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
        KotlinPermissions.with(this) // where this is an FragmentActivity instance
            .permissions(
                Manifest.permission.CAMERA
            ).onAccepted {
                val zXingScannerView = ZXingScannerView(this)
                this.setContentView(zXingScannerView)
                zXingScannerView.run {
                    startCamera()
                    setResultHandler {
                        stopCamera()
                        val resultString = it.text.toString()
                        Toast.makeText(
                            this@ScanQrActivity, "QR code = $resultString",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }.onDenied {
                Toast.makeText(
                    this, "Permission Denied",
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }
            .onForeverDenied {
                Toast.makeText(
                    this, " Forever Denied",
                    Toast.LENGTH_LONG
                ).show()
            }
            .ask()


    }
}
