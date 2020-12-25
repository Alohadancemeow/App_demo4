package com.example.app_demo4.model

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_home_row.view.*
import kotlinx.android.synthetic.main.recyclerview_member_list_row.view.*

class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var expandableLayout: LinearLayout = itemView.expandable_layout

    //Event card
    fun bind(homeData: HomeData) {
        itemView.tv_card_event_type.text = homeData.event_type
        itemView.tv_event_name.text = homeData.event_name
        itemView.tv_location.text = homeData.event_location
        itemView.tv_time.text = homeData.event_time

    }

    //Member list in event card
    fun memberBind(memberData: MemberData){
        itemView.tv_member_list_name.text = memberData.mid
    }
}