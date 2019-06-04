package com.example.testwithfirebase


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.tbouron.shakedetector.library.ShakeDetector
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.jaredrummler.android.device.DeviceName
import kotlinx.android.synthetic.main.fragment_main.*

class NearByFragment : Fragment()  {
    var mMessageListener: MessageListener? = null
    var mMessage: Message? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_near_by, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btSentNearBy.setOnClickListener {
            activity?.let {
                val message = DeviceName.getDeviceName().toByteArray(
                    Charsets.UTF_8
                )
                mMessage = object : Message(
                    message
                ) {}
                Nearby.getMessagesClient(it).publish(mMessage!!)
                Log.e("== onClick ==",DeviceName.getDeviceName())
            }
        }
        mMessageListener = object : MessageListener() {
            override fun onFound(message: Message?) {
                val content = message?.content?.toString(
                    Charsets.UTF_8
                )
                Toast.makeText(
                    activity, " Found Device === $content",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("Found Device ==  ", content)
            }

            override fun onLost(message: Message?) {
                val content = message?.content?.toString(
                    Charsets.UTF_8
                )
                Toast.makeText(
                    activity, " Lost message = $content",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("Lost Device ==  ", content)
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
        ShakeDetector.stop()
        super.onStop()
    }
}
