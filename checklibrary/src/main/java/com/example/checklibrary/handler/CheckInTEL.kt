package com.example.checklibrary.handler

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.example.checklibrary.Interfaces.CheckInTELCallBack
import com.example.checklibrary.activity.NearByActivity
import com.example.checklibrary.activity.ScanQrActivity
import com.example.checklibrary.activity.ShakeActivity

class CheckInTEL(val context: Context) {

    companion object{  // another class can call this value
        @SuppressLint("StaticFieldLeak")
        var checkInTEL: CheckInTEL? = null
        fun initial(context: Context){
            checkInTEL = CheckInTEL(context)
        }
    }

    var checkInTELCallBack: CheckInTELCallBack? = null

    fun openScanQRCode(){
        val intent = Intent(context,ScanQrActivity::class.java)
        context.startActivity(intent)
    }

    fun openNearBy(){
        val intent = Intent(context,NearByActivity::class.java)
        context.startActivity(intent)
    }

    fun openShake(){
        val intent = Intent(context,ShakeActivity::class.java)
        context.startActivity(intent)
    }
}