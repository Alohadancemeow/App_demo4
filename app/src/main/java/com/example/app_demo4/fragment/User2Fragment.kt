package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo4.R
import com.example.app_demo4.model.UserData
import com.example.app_demo4.model.UserHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user1.*

class User2Fragment : Fragment() {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var userReference: CollectionReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {

        //set properties
        mDatabase = FirebaseFirestore.getInstance()
        userReference = mDatabase.collection("Users")

        /** # ดึง user ทั้งหมดที่มี status = novice */
        val query  = userReference.whereEqualTo("status", "Novice").orderBy("display_name")
        val options = FirestoreRecyclerOptions.Builder<UserData>()
            .setQuery(query, UserData::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirestoreRecyclerAdapter<UserData, UserHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
                return UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_user, parent, false))
            }

            override fun onBindViewHolder(holder: UserHolder, position: Int, model: UserData) {
                holder.bind(model)
            }
        }

        rv_user.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_user.adapter = adapter
    }

}