package com.trueelogistics.staff.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.kotlinpermissions.KotlinPermissions
import com.trueelogistics.staff.R

class ScreenLoginActivity : AppCompatActivity() {

    private var time: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_login)

        KotlinPermissions.with(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).onAccepted {
                if (it.size == 2)
                    countTimer()
            }.onDenied {
                finish()
                Toast.makeText(this, " Permission Location Denied"
                    , Toast.LENGTH_SHORT).show()
            }.ask()

    }


    override fun onPause() {
        super.onPause()
        time?.cancel()
    }

    private fun countTimer(){
        time = object : CountDownTimer(3000, 500) { // 1 second to onTick & 1 minit to onFinish
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                finish()
                val intent = Intent(
                    this@ScreenLoginActivity
                    , LoginActivity::class.java
                )
                startActivity(intent)
            }
        }.start()
    }
}
