package com.trueelogistics.leader

import android.app.Activity
import android.os.Build
import android.util.Log

class ManagePermission(private val activity: Activity?,private val list: List<String> ,private val code:Int){
    fun requestPermission() {
        Log.e("activity == ",activity.toString())
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.requestPermissions(list.toTypedArray(), code)
            }
        }

    }

}