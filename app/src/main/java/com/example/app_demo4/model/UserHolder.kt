package com.example.app_demo4.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_user.view.*

class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(userData: UserData) {
        itemView.tv_user_name_contact.text = userData.display_name
        itemView.tv_user_full_name_contact.text = userData.full_name
    }
}