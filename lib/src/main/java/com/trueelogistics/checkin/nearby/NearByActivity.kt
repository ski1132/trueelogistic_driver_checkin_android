package com.trueelogistics.checkin.nearby

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.jaredrummler.android.device.DeviceName
import com.trueelogistics.checkin.R
import kotlinx.android.synthetic.main.activity_near_by.*

@SuppressLint("Registered")
class NearByActivity : AppCompatActivity() {
    private var mMessageListener: MessageListener? = null
    private var mMessage: Message? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_by)

        val checkPer = Nearby.zza(this)
        if (checkPer)
            Toast.makeText(this, "Comfirm", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "=== Denied ===", Toast.LENGTH_SHORT).show()
        btSentNearBy.setOnClickListener {
            this.let {
                val message = DeviceName.getDeviceName().toByteArray(
                    Charsets.UTF_8
                )
                mMessage = object : Message(
                    message
                ) {}
                Nearby.getMessagesClient(it).publish(mMessage as Message)
            }
        }
        mMessageListener = object : MessageListener() {
            override fun onFound(message: Message?) {
                val content = message?.content?.toString(
                    Charsets.UTF_8
                )
                Toast.makeText(
                    this@NearByActivity, " Found Device === $content",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onLost(message: Message?) {
                val content = message?.content?.toString(
                    Charsets.UTF_8
                )
                Toast.makeText(
                    this@NearByActivity, " Lost message = $content",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMessageListener?.let { mML ->
            Nearby.getMessagesClient(this).subscribe(mML)
        }
    }

    override fun onStop() {
        mMessage?.let { mMS ->
            this.let {
                Nearby.getMessagesClient(it).unpublish(mMS)
            }
        }
        mMessageListener?.let { mML ->
            this.let {
                Nearby.getMessagesClient(it).unsubscribe(mML)
            }
        }
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(
            this, " Permission Denied ",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

}
