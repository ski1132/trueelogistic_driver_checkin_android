package com.trueelogistics.staff

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        nav_view.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frag_main, ScanQrFragment())
            .commit()
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
            R.id.scanQr -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, ScanQrFragment())
                    .commit()
            }
            R.id.shakeFine -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, ShakeFragment())
                    .commit()
            }
            R.id.nearbyFine -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, NearByFragment())
                    .commit()
            }
            R.id.showMap -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, NearByFragment())
                    .commit()
            }
            R.id.history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, HistoryFragment())
                    .commit()
            }
            R.id.absent -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frag_main, AbsentFragment())
                    .commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun actionToolbar() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

}
