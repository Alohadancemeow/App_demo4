package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.arrayMapOf
import androidx.collection.arraySetOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo4.R
import com.example.app_demo4.model.HomeData
import com.example.app_demo4.model.HomeHolder
import com.example.app_demo4.model.MemberData
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.recyclerview_home_row.*
import kotlinx.android.synthetic.main.recyclerview_home_row.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeFragment : Fragment() {

    //Firebase Properties
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase : FirebaseFirestore
    private lateinit var eventReference: CollectionReference
    private lateinit var memberReference: DocumentReference

    private lateinit var memberListReference: Query
    private lateinit var memberNameReference: DocumentReference

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
        mAuth = FirebaseAuth.getInstance()
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

                holder.itemView.apply {

                    val isExpandable: Boolean = snapshots[position].expandable
                    holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

                    //Button expand
                    this.expand_btn.setOnClickListener {
                        val eventCardList = snapshots[position]
                        eventCardList.expandable = !eventCardList.expandable
                        notifyItemChanged(position)
                    }

                    //Button join
                    this.join_btn.setOnClickListener {

                        val userId = mAuth.currentUser!!.uid
                        val eventId = snapshots.getSnapshot(position).id

//                        memberReference = mDatabase.collection("Events").document(eventId)
//                            .collection("member_list").document(userId)

                        memberReference = mDatabase.collection("Events").document(eventId)

                        val userRef = mDatabase.collection("Users").document(userId)
                        userRef.addSnapshotListener { value, error ->
                            if (error != null) return@addSnapshotListener

                            //get username from uid
                            val name = value?.get("display_name").toString()

//                            val mId  = HashMap<String,Any>().apply {
//                                this["mid"] = name
//                            }

                            val mId  = HashMap<String,Any>().apply {
                                this["mem_list"] = ArrayList<String>().apply {
                                    this.add(name)
                                }
                            }

                            //Join without condition (if) !!
                            memberReference.update(mId).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(context, "You're joined ${snapshots[position].event_name}", Toast.LENGTH_SHORT).show()
//                                    setUpMemberListRecyclerView(userId, eventId)
                                } else {
                                    Toast.makeText(context, "error joined", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                }
            }


        }

        //set recyclerView layout and adapter
        rv_home.layoutManager = linearLayoutManager
        rv_home.adapter = adapter

    }

    // Failed !!
    private fun setUpMemberListRecyclerView(userId: String, eventId: String) {

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        memberListReference = mDatabase.collectionGroup("member_list")

        val query = memberListReference.orderBy("mid")
        val options = FirestoreRecyclerOptions.Builder<MemberData>()
            .setQuery(query, MemberData::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirestoreRecyclerAdapter<MemberData, HomeHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
                return HomeHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_member_list_row, parent, false))
            }

            override fun onBindViewHolder(holder: HomeHolder, position: Int, model: MemberData) {
                holder.memberBind(model)
            }

        }

        //set recyclerView layout and adapter
        rv_member_list.layoutManager = linearLayoutManager
        rv_member_list.adapter = adapter

    }


    private fun getToday() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

}