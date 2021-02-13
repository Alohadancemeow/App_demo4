package com.example.app_demo4.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.app_demo4.R
import com.example.app_demo4.activity.EventReviewActivity
import com.google.firebase.firestore.FirebaseFirestore
import io.karn.notify.Notify

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

//        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        val eventId = intent.extras!!.get("eventId").toString()
        Log.d("TAG", "onReceive: eventId $eventId")

        val eventTime = intent.extras!!.get("eventTime").toString()
        Log.d("TAG", "onReceive: eventTime $eventTime")

        val eventMeetAt = intent.extras!!.get("eventMeetAt").toString()
        Log.d("TAG", "onReceive: eventMeetAt $eventMeetAt")


        val mDatabase = FirebaseFirestore.getInstance()
        val eventRef = mDatabase.collection("Events").document(eventId)
        eventRef.addSnapshotListener { value, _ ->

            value.let {

                //get event name
                val eventName = value?.get("event_name").toString()
                Log.d("TAG", "onReceive: eventName $eventName")


                when (intent.action) {
                    Constants.ACTION_SET_EXACT_ALARM -> {
                        buildNotification(
                            context,
                            eventId,
                            eventName,
                            eventTime,
                            eventMeetAt
                        )
                    }
//                    Constants.ACTION_SET_REPETITIVE_ALARM -> {
//                        val cal = Calendar.getInstance().apply {
//                            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
//                        }
//                        AlarmService(context).setRepetitiveAlarm(cal.timeInMillis)
//                        buildNotification(context, "Set Repetitive Time", convertDate(cal.timeInMillis))
//                    }
                }
            }
        }


    }


    private fun buildNotification(
        context: Context,
        eventId: String,
        title: String,
        eventTime: String,
        eventMeetAt: String
    ) {

        //LargeIcon.
        val bitmap = BitmapFactory.decodeFile(R.drawable.event.toString())

        //Action
        val intentForEvent = Intent(context, EventReviewActivity::class.java).apply {
            this.putExtra("eventId", eventId)
        }

        //PendingIntent
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intentForEvent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val action: NotificationCompat.Action =
            NotificationCompat.Action.Builder(0, "Go to event", pendingIntent).build()

        Notify
            .with(context)
            .actions {
                this.add(action)
            }
            .asBigText {
                this.title = title
                this.text = "Your event is ongoing now"
                this.expandedText = "Meet at $eventMeetAt"
                this.bigText = "Start : $eventTime"
//                this.largeIcon = bitmap
            }
            .header {
                this.color = context.resources.getColor(R.color.colorAccent)
                this.icon = R.drawable.event
            }
            .meta {
                this.cancelOnClick = true
                this.sticky = false
            }
            .show()

    }

    private fun convertDate(timeInMillis: Long): String =
        android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()


}