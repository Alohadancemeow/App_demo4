package com.example.app_demo4.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class MyBroadcastReceiver : BroadcastReceiver() {


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {

        val intent = Intent(context, MyService::class.java)
        context.startService(intent)

    }

}