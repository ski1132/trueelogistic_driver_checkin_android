package com.trueelogistics.staff.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.trueelogistics.staff.R
import com.trueelogistics.staff.fragment.ProfileShowFragment

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_main, ProfileShowFragment())
            .commit()
    }
}
