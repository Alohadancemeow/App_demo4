package com.example.app_demo4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_demo4.R
import com.example.app_demo4.model.UserData
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.recyclerview_user.view.*

class UserRecyclerViewAdapter(options: FirestoreRecyclerOptions<UserData>) :
    FirestoreRecyclerAdapter<UserData, UserRecyclerViewAdapter.UserViewHolder>(options) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: UserData) {
//        holder.tvDisplayName.text = model.displayName
//        holder.tvFullName.text = model.fullName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_user, parent, false)
        return UserViewHolder(view)
    }


    // viewHolder
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(userData: UserData){
            itemView.tv_user_name_contact.text = userData.display_name
            itemView.tv_user_full_name_contact.text = userData.full_name
        }


    }


}