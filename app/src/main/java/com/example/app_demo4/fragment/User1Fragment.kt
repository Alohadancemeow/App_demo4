package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_demo4.R
import com.example.app_demo4.model.UserData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_user.view.*


class User1Fragment : Fragment() {

//    private lateinit var query: Query
    private lateinit var adapter: FirebaseRecyclerAdapter<UserData, ContactViewHolder>

    // Properties
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var contactView : View
    private lateinit var myContactList : RecyclerView

    private lateinit var contactRef : DatabaseReference
    private lateinit var userRef : DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    private lateinit var currentUserId : String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        contactView = inflater.inflate(R.layout.fragment_user1, container, false)
        myContactList = contactView.findViewById(R.id.rv_user)
        myContactList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)

        mAuth = FirebaseAuth.getInstance()
        currentUserId = mAuth.currentUser!!.uid
        contactRef = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserId)
        userRef = FirebaseDatabase.getInstance().reference.child("Users")

        return contactView
    }

    override fun onStart() {
        super.onStart()

        val option = FirebaseRecyclerOptions.Builder<UserData>()
            .setQuery(contactRef, UserData::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<UserData, ContactViewHolder>(option){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
                val view : View = LayoutInflater.from(context).inflate(R.layout.recyclerview_user, parent,false)
                return ContactViewHolder(view)
            }

            override fun onBindViewHolder(holder: ContactViewHolder, position: Int, model: UserData) {

                val userId :String = getRef(position).key!!

                userRef.child(userId).addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild("display_name")){
//                            val image = snapshot.child("image").value.toString()
                            val displayName = snapshot.child("display_name").value.toString()
                            val fullName = snapshot.child("full_name").value.toString()

                            holder.bind(UserData(displayName,fullName))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }

        }
        myContactList.adapter = adapter
        adapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
    }

    inner class ContactViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bind(userData: UserData){
            itemView.tv_user_name_contact?.text = userData.displayName
            itemView.tv_user_full_name_contact?.text = userData.fullName
//            Picasso.get().load(userData.image).placeholder(R.drawable.ic_baseline_account_circle_24).into(itemView.iv_user_contact)

//        Glide.with().load(userData.image).into(customView.iv_user_contact)
        }
    }




//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//        // set properties
//        mDatabase = FirebaseDatabase.getInstance()
//        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        val query = mDatabase.reference.child("Users").orderByChild("display_name")
//        val option = FirebaseRecyclerOptions.Builder<UserData>()
//            .setQuery(query, UserData::class.java).setLifecycleOwner(this).build()
//
//
//
//        adapter = object : FirebaseRecyclerAdapter<UserData, UserHolder>(option) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
//                return UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_user, parent, false))
//            }
//
//            override fun onBindViewHolder(holder: UserHolder, position: Int, model: UserData) {
//                holder.bind(model)
//            }
//
//        }
//
//        //set recyclerview
//        rv_user.setHasFixedSize(true)
//        rv_user.layoutManager = layoutManager
//        rv_user.adapter = adapter
//
//    }

}