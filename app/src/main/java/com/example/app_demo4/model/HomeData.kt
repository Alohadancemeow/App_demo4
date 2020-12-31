package com.example.app_demo4.model

data class HomeData(
    var event_type: String? = null,
    var event_name: String? = null,
    var event_location: String? = null,
    var event_time: String? = null,
    var event_member: String? = null,
    var event_meet: String? = null,
    var event_creator: String? = null,
    var expandable: Boolean = false
) {}