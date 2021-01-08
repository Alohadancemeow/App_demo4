package com.example.app_demo4.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.app_demo4.R
import com.example.app_demo4.activity.EventReviewActivity
import com.example.app_demo4.activity.ProfileReviewActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class NotificationDemoActivity : AppCompatActivity() {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userId: String

    // DateTime Properties
    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    // Notification Properties
    private val channelID = "NotificationDemo"
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_demo)

        //set firebase properties
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        userId = mAuth.currentUser!!.uid

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(channelID, "DemoChannel", "This is notification demo")


        getEventId()

        //Button notify
//        notify_btn.setOnClickListener {
//            displayNotification("Today")
//        }

    }


    private fun getToday() {

        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH) //Start at 0
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR_OF_DAY) // 24 hr
        minute = cal.get(Calendar.MINUTE)
    }

    private fun getEventId() {

        getToday()
        val today = "$day/${month + 1}/$year"  // 6/1/2021

        val eventRef = mDatabase.collection("Events")
            .whereEqualTo("event_date", today).orderBy("event_time")

        eventRef.addSnapshotListener { value, error ->
            error.let {

                //Loop and send eventId
                value?.forEach {

                    val eventId = it.id
                    Log.d("event-id", it.id)

                    getEventName(eventId)
                }

            }
        }
    }

    private fun getEventName(eventId: String) {
        val eventRef = mDatabase.collection("Events").document(eventId)
        eventRef.addSnapshotListener { value, error ->

            error.let {
                val eventName = value?.data?.get("event_name").toString()
                val eventTime = value?.data?.get("event_time").toString()
//                Log.d("event-name", "$eventName + $eventTime")

                getEventMemList(eventName, eventTime, eventId)
            }
        }
    }

    private fun getEventMemList(eventName: String, eventTime: String, eventId: String) {

//        Log.d("userId", userId)
//        Log.d("eventId", eventId)
//        Log.d("eventName", eventName)
//        Log.d("eventTime", eventTime)

        val eventMemListRef = mDatabase.collection("Event-mem-list").document(eventName)
        eventMemListRef.addSnapshotListener { value, error ->

            error.let {
                val memId = value?.data?.keys
                Log.d("user-memId", memId.toString())

                getToday()
                val currentTime = "$hour : $minute" // 1 : 30

                Log.d("time-current", currentTime)
                Log.d("time-event", eventTime)
                val a = eventTime == currentTime
                Log.d("time-a", a.toString())

                //todo : notify to member
                //1.Check time.
                if (eventTime == currentTime && memId?.contains(userId) == true) {

                    //2.Call notify.
                    displayNotification(eventName, eventId)

                } else {

                    Log.d("error","error! time and userId mismatch")

                }
            }
        }
    }


    private fun displayNotification(eventName: String, eventId: String) {

        val notificationId = 45

        //Action
        val intent = Intent(this, EventReviewActivity::class.java).apply {
            this.putExtra("eventId", eventId)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val action: NotificationCompat.Action =
            NotificationCompat.Action.Builder(0, "Go to event", pendingIntent).build()

        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle(eventName)  //show eventName
            .setContentText("$eventName is starting")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setAutoCancel(true)
            .setSubText("Notification")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
            .addAction(action)
            .build()
        notificationManager.notify(notificationId, notification)

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