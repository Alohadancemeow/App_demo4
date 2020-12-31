package com.example.app_demo4.model

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_home_row.view.*

class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var expandableLayout: LinearLayout = itemView.expandable_layout

    //Event card
    @SuppressLint("SetTextI18n")
    fun bind(homeData: HomeData) {
        itemView.tv_card_event_type.text = homeData.event_type
        itemView.tv_event_name.text = homeData.event_name
        itemView.tv_location.text = "Location : ${homeData.event_location}"
        itemView.tv_time.text = "Time : ${homeData.event_time}"
        itemView.tv_member_num.text = "Member : ${homeData.event_member}"
        itemView.tv_meet_at.text = "Meet At : ${homeData.event_meet}"
        itemView.tv_creator.text = "Create by : ${homeData.event_creator}"

    }
}