package com.example.app_demo4.notification

import java.util.concurrent.atomic.AtomicInteger

object RandomIntUtil {

    private val seed = AtomicInteger()

    fun getRandomInt() = seed.getAndIncrement()+System.currentTimeMillis().toInt()
}