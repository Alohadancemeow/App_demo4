package com.example.app_demo4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_demo4.R
import com.example.app_demo4.model.EventData

class EventRecyclerviewAdapter(var content: ArrayList<EventData>): RecyclerView.Adapter<EventRecyclerviewAdapter.ViewHolder>() {

    // Property
    private lateinit var mListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_event, parent,false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvEventName.text = content[position].eventName
        holder.tvLocation.text = content[position].location
    }

    override fun getItemCount(): Int {
        return content.size
    }


    // inner class
    class ViewHolder (itemView :View, var mListener: OnItemClickListener) :RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var tvEventName: TextView = itemView.findViewById(R.id.tv_event_name_list)
        var tvLocation: TextView = itemView.findViewById(R.id.tv_event_location_list)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (mListener != null){
                mListener.setOnClickListener(adapterPosition)
            }
        }
    }


    // interface
    interface OnItemClickListener {
        fun  setOnClickListener(adapterPosition: Int)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener){
        this.mListener = mListener
    }
}