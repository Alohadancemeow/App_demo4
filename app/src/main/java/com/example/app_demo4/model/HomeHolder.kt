package com.example.app_demo4.model

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app_demo4.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.progress_btn_layout.*
import kotlinx.android.synthetic.main.progress_btn_layout.view.*
import kotlinx.android.synthetic.main.recyclerview_home_row.view.*

class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mDatabase = FirebaseFirestore.getInstance()

    var expandableLayout: LinearLayout = itemView.expandable_layout

    //Event card
    @SuppressLint("SetTextI18n")
    fun bind(homeData: HomeData) {

        // At include layout button
        itemView.join_progressbar_button.apply {

            //set text and icon on Button
            val textViewButton: MaterialButton = textView_progress
            textViewButton.apply {
                this.text = "Join"
                this.setIconResource(R.drawable.ic_check_white)
            }
        }


        itemView.tv_card_event_type.text = homeData.event_type
        itemView.tv_event_name.text = homeData.event_name
        itemView.tv_location.text = "Location : ${homeData.event_location}"
        itemView.tv_time.text = "Time : ${homeData.event_time}"
        itemView.tv_member_num.text = "Member : ${homeData.event_member}"
        itemView.tv_meet_at.text = "Meet At : ${homeData.event_meet}"

        //get event creator id
        val eventCreatorId = homeData.event_creator.toString()
        val userRef = mDatabase.collection("Users").document(eventCreatorId)
        userRef.addSnapshotListener { value, _ ->

            value.let {

                //get creator's name
                val creatorName = value?.get("display_name").toString()
                Log.d("TAG", "bind: creatorName $creatorName")

                //set to view
                itemView.tv_creator.text = "Create by : $creatorName"
            }
        }

    }
}