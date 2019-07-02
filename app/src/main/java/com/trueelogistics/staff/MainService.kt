package com.trueelogistics.staff

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


@Suppress("DEPRECATION")
class MainService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("== in Received ==", "From: " + remoteMessage.from!!)

        if (remoteMessage.data.isNotEmpty()) {
            Log.e("== RM Size ==", "Message data payload: " + remoteMessage.data)
//            val data : Map<String,String> = remoteMessage.data
            showNotification("=== DAta == ",remoteMessage.data.toString())

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e("====", "Message Notification Body: " + remoteMessage.notification!!.body!!)
            showNotification("Heading Tesssst","this is a text")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    private fun showNotification(title: String?, body: String?) {
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this@MainService, 0, intent,0)
        val notification = Notification.Builder(this@MainService)
            .setTicker("")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent).notification

        notification.flags = Notification.FLAG_AUTO_CANCEL
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
    override fun onMessageSent(p0: String?) {
        Log.e("== onMessageSent == ",p0)
    }
    override fun onNewToken(token: String?) {
       Log.e("token == ",token)
    }
}