package com.example.app_demo4.model


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_demo4.fragment.User1Fragment
import kotlinx.android.synthetic.main.recyclerview_user.view.*

class UserHolder(private val customView: View) : RecyclerView.ViewHolder(customView){

    fun bind(userData: UserData){
        customView.tv_user_name_contact?.text = userData.displayName
        customView.tv_user_full_name_contact?.text = userData.fullName
//        Glide.with().load(userData.image).into(customView.iv_user_contact)
    }

}