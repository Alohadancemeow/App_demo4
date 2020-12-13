package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo4.R
import com.example.app_demo4.model.EventData
import com.example.app_demo4.model.EventHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_event1.*

class Event1Fragment : Fragment() {

    //Firebase Properties
    private lateinit var mDatabase : FirebaseFirestore
    private lateinit var eventReference: CollectionReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event1, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //set properties
        mDatabase = FirebaseFirestore.getInstance()
        eventReference = mDatabase.collection("Events")

        /** # ดึง event ทั้งหมดที่มี event type = Event A โดยเรียงตามวันที่ */
        val query = eventReference.whereEqualTo("event_type", "Event A").orderBy("event_date")
        val options = FirestoreRecyclerOptions.Builder<EventData>()
            .setQuery(query, EventData::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirestoreRecyclerAdapter<EventData, EventHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
                return EventHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_event,parent,false))
            }

            override fun onBindViewHolder(holder: EventHolder, position: Int, model: EventData) {

                holder.bind(model)

                //get key (document ID) from FirestoreRecyclerAdapter
                val userId = snapshots.getSnapshot(position).id

                holder.itemView.apply {
                    setOnClickListener {
                        Toast.makeText(context, "${model.event_name} is clicked", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        //set recyclerView layout and adapter
        rv_event.layoutManager = linearLayoutManager
        rv_event.adapter = adapter

    }

}