package com.example.testwithfirebase

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.checklibrary.Interfaces.CheckInTELCallBack
import com.example.checklibrary.handler.CheckInTEL
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : AppCompatActivity(), CheckInTELCallBack {

    override fun onCheckInSuccess(result: String) {

    }

    override fun onCheckInFailure(message: String) {

    }

    override fun onCancel() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CheckInTEL.checkInTEL? = this
        btScanQR.setOnClickListener {
            CheckInTEL.checkInTEL?.openScanQRCode(af)
        }
        btSentNearBy.setOnClickListener {
            CheckInTEL.checkInTEL?.openNearBy()
        }
        btShake.setOnClickListener {
            CheckInTEL.checkInTEL?.openShake()
        }
    }

}
