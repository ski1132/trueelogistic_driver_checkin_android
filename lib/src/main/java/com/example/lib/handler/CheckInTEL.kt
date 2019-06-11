package com.example.lib.handler

import android.annotation.SuppressLint
import android.content.Context
import com.example.lib.Interfaces.CheckInTELCallBack
import com.example.lib.activity.NearByActivity
import com.example.lib.activity.ScanQrActivity
import com.example.lib.activity.ShakeActivity

class CheckInTEL(val context: Context) {

    companion object{
        @SuppressLint("StaticFieldLeak")
        var checkInTEL: CheckInTEL? = null
        var checkInTELCallBack: CheckInTELCallBack? = null
        fun intitial(context: Context){
            checkInTEL = CheckInTEL(context)
        }
    }

    fun openScanQRCode(){
        ScanQrActivity()
    }

    fun openNearBy(){
        NearByActivity()
    }

    fun openShake(){
        ShakeActivity()
    }
}