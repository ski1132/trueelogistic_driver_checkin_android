package com.example.lib.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lib.R
import me.dm7.barcodescanner.zxing.ZXingScannerView

@SuppressLint("Registered")
class ScanQrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

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
            }
        }
    }
}
