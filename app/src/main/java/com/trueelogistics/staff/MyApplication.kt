package com.trueelogistics.staff

import android.app.Application
import com.trueelogistics.checkin.handler.CheckInTEL

class MyApplication: Application(){ //open this activity name before another

    override fun onCreate() {
        super.onCreate()
        CheckInTEL.initial(this) // initial 1 time because is use a lot of mem and value
        // can use all class in this app
    }
}