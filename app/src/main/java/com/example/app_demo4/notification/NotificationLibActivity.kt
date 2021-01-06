package com.example.app_demo4.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.app_demo4.R
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_notification_lib.*

class NotificationLibActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_lib)

        alerter_btn.setOnClickListener {

            Alerter.Companion.create(this)
                .setTitle("Title")
                .setText("Notification from app-demo4")
                .setIcon(R.drawable.ic_event_available_white)
                .setBackgroundColorRes(R.color.colorAccent)
                .setDuration(4000)
                .setOnClickListener {
                    Toast.makeText(applicationContext, "Alert clicked", Toast.LENGTH_SHORT).show()
                }
                .setSound()
                .enableSwipeToDismiss()
                .show()
        }
    }
}