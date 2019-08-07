package com.trueelogistics.staff.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.orhanobut.hawk.Hawk
import com.trueelogistics.staff.R

class ScreenLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_login)

        object : CountDownTimer(3000, 500) { // 1 second to onTick & 1 minit to onFinish
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
