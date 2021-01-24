package com.example.app_demo4.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo4.R
import com.example.app_demo4.activity.EventReviewActivity
import com.example.app_demo4.model.HomeData
import com.example.app_demo4.model.HomeHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_event_review.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.recyclerview_home_row.*
import kotlinx.android.synthetic.main.recyclerview_home_row.view.*
import java.util.*
import kotlin.collections.HashMap


class HomeFragment : Fragment() {

    //Firebase Properties
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var eventReference: CollectionReference
    private lateinit var memberReference: DocumentReference


    // Date Properties
    private var day = 0
    private var month = 0
    private var year = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //set properties
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        setUpRecyclerView()

    }


    private fun setUpRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        eventReference = mDatabase.collection("Events")

        /** # ดึง event ทั้งหมดที่ตรงกับวันนี้ โดยเรียงตามเวลา */

        getToday()
        val today = "$day/${month + 1}/$year"

        val query = eventReference.whereEqualTo("event_date", today).orderBy("event_time")
        val options = FirestoreRecyclerOptions.Builder<HomeData>()
            .setQuery(query, HomeData::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirestoreRecyclerAdapter<HomeData, HomeHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
                return HomeHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recyclerview_home_row, parent, false)
                )
            }

            override fun onBindViewHolder(holder: HomeHolder, position: Int, model: HomeData) {
                holder.bind(model)

                val eventName = snapshots[position].event_name.toString()

                holder.itemView.apply {

                    val isExpandable: Boolean = snapshots[position].expandable
                    holder.expandableLayout.visibility =
                        if (isExpandable) View.VISIBLE else View.GONE

                    //Button expand
                    this.expand_btn.setOnClickListener {
                        val eventCardList = snapshots[position]
                        eventCardList.expandable = !eventCardList.expandable
                        notifyItemChanged(position)
                    }

                    //Button More Details
                    this.details_btn.setOnClickListener {
                        val eventId = snapshots.getSnapshot(position).id
                        val intent = Intent(context, EventReviewActivity::class.java)
                        intent.putExtra("eventId", eventId)
                        startActivity(intent)
                    }

                    //Button join
                    this.join_btn.setOnClickListener {

                        val userId = mAuth.currentUser!!.uid

                        //Create Event-mem-list by event name
                        memberReference = mDatabase.collection("Event-mem-list").document(eventName)


                        val userRef = mDatabase.collection("Users").document(userId)
                        userRef.addSnapshotListener { value, error ->
                            if (error != null) return@addSnapshotListener

                            //get username from uid
                            val name = value?.get("display_name").toString()

                            val mId = HashMap<String, Any>().apply {
                                this[userId] = name
                            }

                            //Join with condition (if) !!
                            memberReference.apply {
                                addSnapshotListener { value, error ->

                                    error.let {

                                        val memId = value?.data?.keys //userId in eventId
//                                        Log.d("memId", memId.toString())
//                                        Log.d("mId", userId)

                                        if (memId?.contains(userId) == true) {

                                            //Why always show ?
                                            Snackbar.make(root_layout_home_fm,"You're joined already", Snackbar.LENGTH_LONG)
                                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                                .setAction("View") {
                                                    val eventId = snapshots.getSnapshot(position).id
                                                    val intent = Intent(context, EventReviewActivity::class.java)
                                                    intent.putExtra("eventId", eventId)
                                                    startActivity(intent)
                                                }
                                                .show()

                                        } else {
                                            //set data(mId) to firebase
                                            set(mId, SetOptions.mergeFields(userId)).addOnCompleteListener {
                                                if (it.isSuccessful) {

                                                    Snackbar.make(root_layout_home_fm,"You're joined ${snapshots[position].event_name}", Snackbar.LENGTH_LONG)
                                                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                                        .show()

//                                                    Toast.makeText(context, "You're joined ${snapshots[position].event_name}", Toast.LENGTH_SHORT)
//                                                        .show()
                                                } else {
                                                    Toast.makeText(context, "error join", Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            }
                                        }
                                    }
                                }
                            } //end apply.
                        }
                    } //end join btn.

                }
            } //end onBindViewHolder.
        } //end adapter.


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