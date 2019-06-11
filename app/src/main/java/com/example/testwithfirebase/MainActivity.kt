package com.example.testwithfirebase

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.contentMainFrag, MainFragment()).commit()

        btScanQR.setOnClickListener {
                KotlinPermissions.with(this) // where this is an FragmentActivity instance
                    .permissions(
                        Manifest.permission.CAMERA
                    ).onAccepted {

                    }.onDenied {
                        Toast.makeText(
                            this, "Permission Denied",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .onForeverDenied {
                        Toast.makeText(
                            this, " Forever Denied",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .ask()

        }
        btSentNearBy.setOnClickListener {
            supportFragmentManager?.beginTransaction()?.replace(R.id.contentMainFrag, NearByFragment())
                ?.addToBackStack(null)?.commit()
        }
        btShake.setOnClickListener {
            supportFragmentManager?.beginTransaction()?.replace(R.id.contentMainFrag, ShakeFragment())
                ?.addToBackStack(null)?.commit()
        }
    }

}
