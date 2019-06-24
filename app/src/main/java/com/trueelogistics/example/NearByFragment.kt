package com.trueelogistics.example


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.jaredrummler.android.device.DeviceName
import kotlinx.android.synthetic.main.fragment_near_by.*

class NearByFragment : Fragment()  {
    private var mMessageListener: MessageListener? = null
    private var mMessage: Message? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                Nearby.getMessagesClient(it).publish(mMessage as Message)
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
            }

            override fun onLost(message: Message?) {
                val content = message?.content?.toString(
                    Charsets.UTF_8
                )
                Toast.makeText(
                    activity, " Lost message = $content",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    override fun onStart() {
        super.onStart()

        mMessageListener?.let { mML ->
            activity?.let {
                Nearby.getMessagesClient(it).subscribe(mML)
            }
        }

    }

    override fun onStop() {
        mMessage?.let { mMS ->
            activity?.let {
                Nearby.getMessagesClient(it).unpublish(mMS)
            }
        }
        mMessageListener?.let { mML ->
            activity?.let {
                Nearby.getMessagesClient(it).unsubscribe(mML)
            }
        }
        super.onStop()
    }
}
