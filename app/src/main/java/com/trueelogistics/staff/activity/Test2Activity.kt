package com.trueelogistics.staff.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.trueelogistics.staff.R

class Test2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        Toast.makeText(
            this,
            "onFailure",
            Toast.LENGTH_SHORT
        ).show()
    }
}
