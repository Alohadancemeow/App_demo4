//package com.example.app_demo4.service
//
//import android.app.NotificationManager
//import android.content.Context
//import android.media.RingtoneManager
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.example.app_demo4.R
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//
//class MyFirebaseMessagingService: FirebaseMessagingService() {
//
//    private val TAG = "FCM"
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.d(TAG, token)
//    }
//
//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//        Log.d(TAG, "Payloads : ${message.data}")
//
//        if (message.notification != null){
//            showNotification(message.notification!!.title, message.notification!!.body)
//
//        }
//
//    }
//
//    private fun showNotification(title: String?, body: String?) {
//
//        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notification = NotificationCompat.Builder(this)
//            .setSmallIcon(R.drawable.ic_app_icon)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setSound(sound)
//            .setAutoCancel(true)
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(0, notification.build())
//
//    }
//}