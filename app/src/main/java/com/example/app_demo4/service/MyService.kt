package com.example.app_demo4.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.app_demo4.R


class MyService : Service() {

    // Notification Properties
    private val channelID = "NotificationDemo"
    private lateinit var notificationManager: NotificationManager


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID, "DemoChannel", "This is notification demo")


        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(baseContext, channelID)
            .setSmallIcon(R.drawable.ic_app_icon)
            .setContentTitle("My app")
            .setContentText("Hello from service")
            .setSound(uri)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(23, notification)


//        return super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY

    }


    private fun createNotificationChannel(id: String, name: String, desc: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = desc
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


}