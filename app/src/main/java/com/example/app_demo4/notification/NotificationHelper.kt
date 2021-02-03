package com.example.app_demo4.notification

import android.R
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.app_demo4.activity.EventReviewActivity

class NotificationHelper(base: Context?) : ContextWrapper(base) {

    private val channelID = "channelID"
    private val channelName = "Channel Name"

    private var mManager: NotificationManager? = null


    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            channelID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH)
        getManager().createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager {
        if (mManager == null) {
            mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return mManager as NotificationManager
    }

    fun getChannelNotification(eventName: String, eventId: String): NotificationCompat.Builder? {

        Log.d("TAG", "getChannelNotification: eventName $eventName")
        Log.d("TAG", "getChannelNotification: eventId $eventId")

        //Action
        val intentForEvent = Intent(baseContext, EventReviewActivity::class.java).apply {
            this.putExtra("eventId", eventId)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            baseContext,
            0,
            intentForEvent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //LargeIcon.
        val bitmap = BitmapFactory.decodeResource(resources, com.example.app_demo4.R.drawable.event)

//        val action: NotificationCompat.Action =
//            NotificationCompat.Action.Builder(0, "Go to event", pendingIntent).build()


        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(eventName)
            .setContentText("Your event is ongoing now")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(com.example.app_demo4.R.drawable.ic_event_available_yellow)
            .setLargeIcon(bitmap)
            .addAction(0, "Go to event", pendingIntent)
            .setColor(resources.getColor(com.example.app_demo4.R.color.colorAccent))
//            .addAction(action)

    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}