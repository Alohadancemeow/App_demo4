package com.example.app_demo4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_demo4.R
import com.example.app_demo4.model.HomeData
import kotlinx.android.synthetic.main.recyclerview_home_row.view.*

class HomeRecyclerviewAdapter(var content: ArrayList<HomeData>) :
    RecyclerView.Adapter<HomeRecyclerviewAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var eventType: TextView = itemView.findViewById(R.id.tv_card_event_type)
        var eventName: TextView = itemView.findViewById(R.id.tv_event_name)
        var location: TextView = itemView.findViewById(R.id.tv_location)
        var time: TextView = itemView.findViewById(R.id.tv_time)

        var expandButton: ImageView = itemView.findViewById(R.id.expand_btn)
        var expandableLayout: LinearLayout = itemView.findViewById(R.id.expandable_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_home_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardData: HomeData = content[position]
        holder.eventType.text = cardData.eventType
        holder.eventName.text = cardData.eventName
        holder.location.text = cardData.location
        holder.time.text = cardData.time


        val isExpandable: Boolean = content[position].expandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE


        holder.expandButton.apply {
            setOnClickListener {
                val eventCardList = content[position]
                eventCardList.expandable = !eventCardList.expandable
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return content.size
    }
}