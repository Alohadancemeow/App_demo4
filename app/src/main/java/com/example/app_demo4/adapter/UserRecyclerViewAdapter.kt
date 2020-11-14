package com.example.app_demo4.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_demo4.R
import com.example.app_demo4.fragment.User1Fragment
import com.example.app_demo4.model.UserData
import kotlinx.android.synthetic.main.recyclerview_user.view.*

class UserRecyclerViewAdapter(private var context: Context): RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder>() {

    private var dataList = mutableListOf<UserData>()

    fun setListData(data : MutableList<UserData>){
        dataList = data
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userData = dataList[position]
        holder.bindView(userData)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) dataList.size else 0
    }

    // viewHolder
    inner class UserViewHolder(itemView :View): RecyclerView.ViewHolder(itemView){

        fun bindView(userData: UserData){
//            Glide.with(context).load(userData.image).into(itemView.iv_user_contact)
            itemView.tv_user_name_contact?.text = userData.displayName
            itemView.tv_user_full_name_contact?.text = userData.fullName
        }
    }
}