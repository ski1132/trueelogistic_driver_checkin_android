package com.trueelogistics.example

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.trueelogistics.checkin.activity.GenQrActivity
import com.trueelogistics.checkin.scanqr.ScanQrActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main_menu.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        checkInBtn.setOnClickListener {
            val intent = Intent(this, ScanQrActivity::class.java)
            this.startActivity(intent)
        }
        checkBetBtn.setOnClickListener {
            val intent = Intent(this, ScanQrActivity::class.java)
            this.startActivity(intent)
        }
        checkOutBtn.setOnClickListener {
            val intent = Intent(this, ScanQrActivity::class.java)
            this.startActivity(intent)
        }
        genQr.setOnClickListener {
            val intent = Intent(this, GenQrActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.shake_fine -> {
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show()
            }
            R.id.nearby_fine -> {
                Toast.makeText(this, "Gallery", Toast.LENGTH_LONG).show()
            }
            R.id.history -> {
                Toast.makeText(this, "sideShow", Toast.LENGTH_LONG).show()
            }
            R.id.absent -> {
                Toast.makeText(this, "absent show", Toast.LENGTH_LONG).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
