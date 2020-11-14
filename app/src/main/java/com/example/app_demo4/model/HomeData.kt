package com.example.app_demo4.model

data class HomeData(
    val eventType: String,
    val eventName: String,
    val location: String,
    val time: String,
    var expandable: Boolean = false
) {
}