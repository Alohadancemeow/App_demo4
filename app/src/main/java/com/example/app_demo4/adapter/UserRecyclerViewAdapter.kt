package com.example.app_demo4.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.app_demo4.R
import com.example.app_demo4.model.UserData
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.recyclerview_user.view.*

class UserRecyclerViewAdapter(private val mContext: Context, private val mUsers: List<UserData>) :
    RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder?>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user: UserData = mUsers[position]
        holder.bind(user)
//        holder.tvDisplayName.text = user.display_name
//        holder.tvFullName.text = user.full_name
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }


    // viewHolder
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvDisplayName: TextView = itemView.tv_user_name_contact
        var tvFullName: TextView = itemView.tv_user_full_name_contact


        fun bind(userData: UserData){
            tvDisplayName.text = userData.display_name
            tvFullName.text = userData.full_name
        }

    }

}



// Never used