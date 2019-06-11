package com.example.lib.handler

import com.example.lib.Interfaces.CheckInTELCallBack
import com.example.lib.activity.NearByActivity
import com.example.lib.activity.ScanQrActivity
import com.example.lib.activity.ShakeActivity

class CheckInTEL {
    init {
        val checkInTEL: CheckInTELCallBack
        ScanQrActivity()
        NearByActivity()
        ShakeActivity()
    }
}