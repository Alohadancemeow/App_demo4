package com.example.app_demo4.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.app_demo4.R
import com.example.app_demo4.activity.EventReviewActivity
import com.example.app_demo4.activity.ProfileReviewActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_notification_demo.*
import java.text.DateFormat
import java.util.*
import kotlin.time.ExperimentalTime

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

    @ExperimentalTime
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_notification_demo)

        //set firebase properties
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        userId = mAuth.currentUser!!.uid

        //Create Notification channel if device is using API 26+
//        createNotificationChannel(channelID, "DemoChannel", "This is notification demo")


        getEventId()

//        Button notify
        notify_btn.setOnClickListener {
            displayNotification("Notification Demo" ,"eventId")
//            alertNotify("eventName", "eventId")
//            startAlarm("eventId")
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    private fun getEventId() {

        //get current date
        val cal = Calendar.getInstance()
        cal.get(Calendar.DAY_OF_MONTH)
        cal.get(Calendar.MONTH)
        cal.get(Calendar.YEAR)
        //format date as 'Fab 2, 2021'
        val today = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.time)
        Log.d("TAG", "getEventId:today $today")

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

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    private fun getEventName(eventId: String) {

        Log.d("TAG", "getEventName: eventId $eventId")

        val eventRef = mDatabase.collection("Events").document(eventId)
        eventRef.addSnapshotListener { value, error ->

            error.let {
                val eventName = value?.data?.get("event_name").toString()
                val eventTime = value?.data?.get("event_time").toString()

                Log.d("TAG", "getEventName: eventName $eventName")
                Log.d("TAG", "getEventName: eventTime $eventTime")

                getEventMemList(eventName, eventTime, eventId)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    private fun getEventMemList(eventName: String, eventTime: String, eventId: String, ) {

        Log.d("TAG", "getEventMemList: $eventId, $eventName, $eventTime")

        val eventMemListRef = mDatabase.collection("Event-mem-list").document(eventName)
        eventMemListRef.addSnapshotListener { value, error ->

            error.let {
                val memId = value?.data?.keys
                Log.d("TAG", "getEventMemList: memIdOf($eventName) $memId")

                //get current time
                val cal = Calendar.getInstance()
                cal.get(Calendar.HOUR_OF_DAY)
                cal.get(Calendar.MINUTE)
                //format time as '1:45 AM'
                val currentTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.time)

                Log.d("TAG", "getEventMemList: currentTime $currentTime")
                Log.d("TAG", "getEventMemList: eventTimeOf($eventName) $eventTime")
                val a = eventTime == currentTime
                Log.d("TAG", "getEventMemList: ==? $a")

                //todo : notify to member
                //Check time.
                if (eventTime == currentTime && memId?.contains(userId) == true) {

                    Log.d("TAG", "getEventMemList: (if) true")

                    //call startAlarm
                    startAlarm(eventId)

                } else {

                    Log.d("error","error! time and userId mismatch")

                }
            }
        }
    }

    private fun startReminder(timeInMillis: Long, eventId: String, eventName: String) {

        val intent = Intent(this, ReminderBroadcast::class.java).apply {
            this.putExtra("eventName", eventName)
            this.putExtra("eventId", eventId)
        }

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val alarmMan = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmMan.set(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }



    //startAlarm.
    @RequiresApi(Build.VERSION_CODES.O)
    fun startAlarm(eventId: String) {
        Log.d("TAG", "startAlarm:eventId $eventId")

        val eventRef = mDatabase.collection("Events").document(eventId)
        eventRef.addSnapshotListener { value, error ->

            error.let {

                val eventName = value?.data?.get("event_name").toString()
                val eventTime = value?.data?.get("event_time")
                Log.d("TAG", "startAlarm:eventTime $eventTime")

                val formatter = DateFormat.getTimeInstance(DateFormat.SHORT)
                val timestamp = formatter.parse(eventTime as String)

                val cal = Calendar.getInstance()
                cal.time = timestamp!!
                Log.d("TAG", "startAlarm:cal $cal")


                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, ReminderBroadcast::class.java).apply {
                    this.putExtra("eventName", eventName)
                    this.putExtra("eventId", eventId)
                }
                val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)

                if (cal.before(Calendar.getInstance())) {
                    cal.add(Calendar.DATE, 1)
                }

                Log.d("TAG", "startAlarm:cal2 ${cal.timeInMillis} ")

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    cal.timeInMillis,
                    pendingIntent)
            }
        }
    }


    // Display Notification. Push
    private fun displayNotification(eventName: String, eventId: String) {

        val notificationId = 45

        //Action
        val intent = Intent(this, EventReviewActivity::class.java).apply {
            this.putExtra("eventId", eventId)
            this.putExtra("eventName", eventName)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

//        val action: NotificationCompat.Action =
//            NotificationCompat.Action.Builder(0, "Go to event", pendingIntent).build()

        //LargeIcon.
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.event)

        //Builder.
        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle(eventName)  //show eventName
            .setContentText("Your event is ongoing now")
//            .setSubText("Notification")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_event_available_yellow)
            .setLargeIcon(bitmap)
//            .setStyle(NotificationCompat.BigPictureStyle()
//                .bigPicture(bitmap)
//                .bigLargeIcon(null))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .addAction(action)
            .addAction(0, "Go to event", pendingIntent)
            .setColor(resources.getColor(R.color.colorAccent))
            .build()

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationId, notification)

    }


}