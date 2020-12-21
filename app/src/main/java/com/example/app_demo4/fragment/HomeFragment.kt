package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo4.R
import com.example.app_demo4.adapter.HomeRecyclerviewAdapter
import com.example.app_demo4.model.EventData
import com.example.app_demo4.model.HomeData
import com.example.app_demo4.model.HomeHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_event1.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.recyclerview_home_row.*
import kotlinx.android.synthetic.main.recyclerview_home_row.view.*
import java.util.*
import java.util.Calendar.MONTH
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    //Firebase Properties
    private lateinit var mDatabase : FirebaseFirestore
    private lateinit var eventReference: CollectionReference

    // Date Properties
    private var day = 0
    private var month = 0
    private var year = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //set properties
        mDatabase = FirebaseFirestore.getInstance()
        eventReference = mDatabase.collection("Events")

        /** # ดึง event ทั้งหมดที่ตรงกับวันนี้ โดยเรียงตามเวลา */

        getToday()
        val today = "$day/${month+1}/$year"

        val query = eventReference.whereEqualTo("event_date", today).orderBy("event_time")
        val options = FirestoreRecyclerOptions.Builder<HomeData>()
            .setQuery(query, HomeData::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirestoreRecyclerAdapter<HomeData, HomeHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
                return HomeHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_home_row, parent, false))
            }

            override fun onBindViewHolder(holder: HomeHolder, position: Int, model: HomeData) {
                holder.bind(model)

                //get key (document ID) from FirestoreRecyclerAdapter
//                val eventId = snapshots.getSnapshot(position).id

                holder.itemView.apply {

                    val isExpandable: Boolean = snapshots[position].expandable
                    holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

                    this.expand_btn.setOnClickListener {
                        val eventCardList = snapshots[position]
                        eventCardList.expandable = !eventCardList.expandable
                        notifyItemChanged(position)
                    }
                }
            }


        }

        //set recyclerView layout and adapter
        rv_home.layoutManager = linearLayoutManager
        rv_home.adapter = adapter

    }

    private fun getToday() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

}