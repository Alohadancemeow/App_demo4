package com.example.app_demo4.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.security.Key

class ReminderBroadcast : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        intent.let {

            val eventId = intent.extras!!.get("eventId").toString()
            Log.d("TAG", "onReceive:eventId $eventId")

            val mDatabase = FirebaseFirestore.getInstance()
            val eventRef = mDatabase.collection("Events").document(eventId)
            eventRef.addSnapshotListener { value, _ ->

                value.let {

                    val eventName = value?.get("event_name").toString()
                    Log.d("TAG", "onReceive: eventName $eventName")


                    val notificationHelper = NotificationHelper(context)
                    val nb: NotificationCompat.Builder? =
                        notificationHelper.getChannelNotification(
                            eventName,
                            eventId
                        )
                    notificationHelper.getManager().notify(1, nb?.build())
                }
            }
        }

    }


}

