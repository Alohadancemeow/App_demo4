package com.example.app_demo4.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_event.view.*

class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(eventData: EventData) {
        itemView.tv_event_name_list.text = eventData.event_name
        itemView.tv_event_date_list.text = eventData.event_date
    }
}