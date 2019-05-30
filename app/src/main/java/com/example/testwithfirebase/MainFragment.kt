package com.example.testwithfirebase

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.fragment_main.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MainFragment : Fragment() {
    var mMessageListener: MessageListener? = null
    var mMessage: Message? = null
    private var zXingScannerView: ZXingScannerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btScanQR.setOnClickListener {
            activity?.let { fragActivity ->
                KotlinPermissions.with(fragActivity) // where this is an FragmentActivity instance
                    .permissions(
                        Manifest.permission.CAMERA
                    ).onAccepted {
                        Toast.makeText(
                            fragActivity, "Permission Access",
                            Toast.LENGTH_LONG
                        ).show()
                        scanQR()
                    }.onDenied {
                        Toast.makeText(
                            fragActivity, "Permission Denied",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .onForeverDenied {
                        Toast.makeText(
                            fragActivity, " Forever Denied",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .ask()
            }
        }
        btSentNearBy.setOnClickListener {
            activity?.let {
                val message = "Hello World".toByteArray(
                    Charsets.UTF_8
                )
                mMessage = object : Message(
                    message
                ) {}
                Nearby.getMessagesClient(it).publish(mMessage!!)
            }

        }
        mMessageListener = object : MessageListener() {
            override fun onFound(message: Message?) {
                val content = message?.content?.toString(
                    Charsets.UTF_8
                )
                Toast.makeText(
                    activity, " Found message = $content",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("Found message == : ", content)
            }

            override fun onLost(message: Message?) {
                val content = message?.content?.toString(
                    Charsets.UTF_8
                )
                Toast.makeText(
                    activity, " Lost message = $content",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("Lost message == : ", content)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        mMessageListener?.let { mML ->
            activity?.let {
                Nearby.getMessagesClient(it).subscribe(mML)
                Log.e("onStart MessageListener", mML.toString())
            }
        }
    }

    override fun onStop() {
        mMessage?.let { mMS ->
            activity?.let {
                Nearby.getMessagesClient(it).unpublish(mMS)
                Log.e(" onStop mMessage ==", mMS.content.toString())
            }
        }
        mMessageListener?.let { mML ->
            activity?.let {
                Nearby.getMessagesClient(it).unsubscribe(mML)
                Log.e("onStop mMessageListener", mML.toString())
            }
        }
        super.onStop()
    }

    fun scanQR() {
        zXingScannerView = ZXingScannerView(context)
        activity?.setContentView(zXingScannerView)
        zXingScannerView?.run {
            startCamera()
            Log.e("== on click ==", "eiei")
            setResultHandler {
                Log.e("=== in Result Handle ==", it.toString())
                stopCamera()
                activity!!.setContentView(R.layout.activity_main)
                val resultString = it.text.toString()
                Toast.makeText(
                    activity, "QR code = $resultString",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("12MarchV1", "QR code ==> $resultString")
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.contentMainFrag, MainFragment()).commit()
            }
        }
    }
}