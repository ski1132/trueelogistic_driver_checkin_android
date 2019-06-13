package com.example.checklibrary.handler

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.checklibrary.Interfaces.CheckInTELCallBack
import com.example.checklibrary.activity.NearByActivity
import com.example.checklibrary.activity.ScanQrActivity
import com.example.checklibrary.activity.ShakeActivity
import com.kotlinpermissions.KotlinPermissions

class CheckInTEL {

    companion object {  // another class can call this value statis

        var checkInTEL: CheckInTEL? = null
        const val KEY_REQUEST_CODE_CHECK_IN_TEL = 1750
        fun initial() {
            checkInTEL = CheckInTEL()
        }
    }
    private var checkInTELCallBack: CheckInTELCallBack? = null // ???

    fun openScanQRCode(activity: Activity,checkInTELCallBack: CheckInTELCallBack) {
        this.checkInTELCallBack = checkInTELCallBack
        val intent = Intent(activity, ScanQrActivity::class.java)
        activity.startActivityForResult(intent, KEY_REQUEST_CODE_CHECK_IN_TEL) // confirm you not from other activity
    }

    fun openNearBy(activity: Activity,checkInTELCallBack: CheckInTELCallBack) {
        val intent = Intent(activity, NearByActivity::class.java)
        activity.startActivity(intent)
    }
    fun openShake(activity: Activity,checkInTELCallBack: CheckInTELCallBack) {
        val intent = Intent(activity, ShakeActivity::class.java)
        activity.startActivity(intent)

    }
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == KEY_REQUEST_CODE_CHECK_IN_TEL){
            if (resultCode == Activity.RESULT_OK){

            }
            else{
                checkInTELCallBack?.onCancel()
            }
        }

    }

}