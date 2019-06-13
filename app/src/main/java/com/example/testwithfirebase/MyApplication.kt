package com.example.testwithfirebase

import android.app.Application
import com.example.checklibrary.handler.CheckInTEL

class MyApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        CheckInTEL.initial() // intitial 1 time because is use a lot of mem and value
        // can use all class in this app
    }
}