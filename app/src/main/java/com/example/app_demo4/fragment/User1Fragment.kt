package com.example.app_demo4.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo4.R
import com.example.app_demo4.activity.ProfileReviewActivity
import com.example.app_demo4.adapter.UserRecyclerViewAdapter
import com.example.app_demo4.model.UserData
import com.example.app_demo4.model.UserHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_user1.*
import kotlinx.android.synthetic.main.recyclerview_user.view.*


class User1Fragment : Fragment() {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var userReference: CollectionReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val me = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = FirebaseFirestore.getInstance().collection("Users").document(me)
        userRef.addSnapshotListener { value, _ ->

            value.let {
                val userName = value?.get("display_name")
                Log.d("name", userName.toString())

                setUpRecyclerView(userName)
            }
        }

    }


    /** Functions here **/
    private fun setUpRecyclerView(userName: Any?) {

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //set properties
        mDatabase = FirebaseFirestore.getInstance()
        userReference = mDatabase.collection("Users")


        /** # ดึง user ทั้งหมดที่มี status = monk */
        val query =
            userReference.whereNotEqualTo("display_name", userName).whereEqualTo("status", "Monk")
                .orderBy("display_name")
        val options = FirestoreRecyclerOptions.Builder<UserData>()
            .setQuery(query, UserData::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirestoreRecyclerAdapter<UserData, UserHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
                return UserHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recyclerview_user, parent, false)
                )
            }

            override fun onBindViewHolder(holder: UserHolder, position: Int, model: UserData) {

                holder.bind(model)

                //get key (document ID) from FirestoreRecyclerAdapter
                val userId = snapshots.getSnapshot(position).id

                holder.itemView.apply {

                    this.setOnClickListener {

                        sendToProfile(userId)

                    }

                    this.iv_user_icon.setOnClickListener {
                        //get phone number
                        val phone = snapshots.getSnapshot(position)["phone"]
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                        startActivity(intent)
                        //send intent to user call app
                    }

                }
            }

        }

        //set recyclerView layout and adapter
        rv_user?.layoutManager = linearLayoutManager
        rv_user?.adapter = adapter
    }

    private fun sendToProfile(userId: String) {
        val intent = Intent(context, ProfileReviewActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

}