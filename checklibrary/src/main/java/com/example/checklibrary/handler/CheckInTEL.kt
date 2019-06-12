package com.example.checklibrary.handler

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.example.checklibrary.Interfaces.CheckInTELCallBack
import com.example.checklibrary.activity.NearByActivity
import com.example.checklibrary.activity.ScanQrActivity
import com.example.checklibrary.activity.ShakeActivity

class CheckInTEL(private val context: Context? = null) {

    companion object {  // another class can call this value
        const val KEY_INTERFACE_CHECK_IN_TEL = "KEY_INTERFACE_CHECK_IN_TEL"
        @SuppressLint("StaticFieldLeak")
        var checkInTEL: CheckInTEL? = null

        fun initial(context: Context) {
            checkInTEL = CheckInTEL(context)
        }
    }

    var checkInTELCallBack: CheckInTELCallBack? = null

    fun openScanQRCode() {
        val intent = Intent(context, ScanQrActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.putExtras( Bundle().apply { this.putSerializable(KEY_INTERFACE_CHECK_IN_TEL, checkInTELCallBack)})
        context?.startActivity(intent)
    }

    fun openNearBy() {
        val intent = Intent(context, NearByActivity::class.java)
        context?.startActivity(intent)
    }

    fun openShake() {
        val intent = Intent(context, ShakeActivity::class.java)
        context?.startActivity(intent)
    }
}