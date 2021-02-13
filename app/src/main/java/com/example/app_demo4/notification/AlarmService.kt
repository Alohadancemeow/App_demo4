package com.example.app_demo4.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class AlarmService(private val context: Context) {

    private val alarmManager: AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    //Firebase
    private lateinit var mDatabase: FirebaseFirestore


    fun setExactAlarm(timeInMillis: Long, eventId: String){

        mDatabase = FirebaseFirestore.getInstance()
        val eventRef = mDatabase.collection("Events").document(eventId)
        eventRef.addSnapshotListener { value, _ ->

            value.let {

                val eventTime = value?.get("event_time").toString()
                Log.d("TAG", "setExactAlarm: eventTime $eventTime")

                val eventMeetAt = value?.get("event_meet").toString()
                Log.d("TAG", "setExactAlarm: meetAt $eventMeetAt")

                setAlarm(
                    timeInMillis,
                    getPendingIntent(
                        getIntent().apply {
                            action = Constants.ACTION_SET_EXACT_ALARM
//                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                            putExtra("eventId", eventId)
                            putExtra("eventTime", eventTime)
                            putExtra("eventMeetAt", eventMeetAt)
                        }
                    )
                )
            }
        }



    }

    //every week
//    fun setRepetitiveAlarm(timeInMillis: Long){
//
//        setAlarm(
//            timeInMillis,
//            getPendingIntent(
//                getIntent().apply {
//                    action = Constants.ACTION_SET_REPETITIVE_ALARM
//                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
//                }
//            )
//        )
//
//    }

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent){

        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            RandomIntUtil.getRandomInt(), // is requestCode
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
}