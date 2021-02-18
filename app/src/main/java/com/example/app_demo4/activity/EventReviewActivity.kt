package com.example.app_demo4.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.app_demo4.R
import com.example.app_demo4.model.ProgressButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_event_review.*
import kotlinx.android.synthetic.main.progress_btn_layout.*


class EventReviewActivity : AppCompatActivity() {

    //Firebase Property
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var memberReference: DocumentReference

    //Top Appbar
    private lateinit var topAppBar: MaterialToolbar

    //progressbar view
    private lateinit var progressBarEventReview: View


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_event_review)

        // set property
        mDatabase = FirebaseFirestore.getInstance()

        // set Top appbar
        topAppBar = top_AppBar_event_preview

        // set progressbar view
        progressBarEventReview = event_review_progressbar_button


        //set text and icon on Button
        val textViewButton: MaterialButton = textView_progress
        textViewButton.apply {
            this.text = "Delete"
            this.setIconResource(R.drawable.ic_delete)
        }


        // Receive intent from Event 1/2 Fragments and HomeFragment
        intent.let {

            val eventId = intent.extras!!.get("eventId").toString()
            Log.d("TAG", "onCreate: eventId $eventId")

            //setup event's data
            setUpEventData(eventId)

            //Top appbar delete btn
            topAppBarAction(eventId)

            progressBarEventReview.setOnClickListener {

                //click on delete btn
                deleteEvent(eventId, it)

            }

        }

    }

    private fun deleteEvent(eventId: String, view: View) {

        //get UID
        val mAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser!!.uid
        Log.d("TAG", "deleteEvent: UID $currentUserId")


        //get event's creator ID
        val eventRef = mDatabase.collection("Events").document(eventId)
        eventRef.addSnapshotListener { value, _ ->

            value.let {

                //get CID
                val eventCreatorId = value?.get("event_creator").toString()
                Log.d("TAG", "deleteEvent: EID $eventCreatorId")

                //UID == CID ?
                Log.d("TAG", "deleteEvent: UID == EID ${currentUserId == eventCreatorId}")


                //show dialog 1
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete this event")
                    .setMessage("Are you sure you want to delete ?")
                    .setNegativeButton("Cancel") { _, _ ->
                        //Nothing on
                    }
                    .setPositiveButton("Delete") { _, _ ->

                        //call progressbar
                        val progressButton = ProgressButton(view)
                        progressButton.buttonActivated(9)

                        //delay
                        val handler = Handler()
                        handler.postDelayed({

                            //check that UID == CID ?
                            if (currentUserId == eventCreatorId) {

                                // Delete event
                                eventRef.delete().addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "delete successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }

                            } else {

                                //UID != CID
                                //show dialog 2
                                MaterialAlertDialogBuilder(this)
                                    .setTitle("Cannot delete")
                                    .setMessage("You are not the creator of this event")
                                    .setPositiveButton("Okay, I got it") { _, _ ->
                                        //Nothing on
                                    }.show()
                            }

                            //end progressbar
                            progressButton.buttonFinished(9)

                        }, 2000)

                    }
                    .show()

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpEventData(eventId: String) {

        Log.d("TAG", "setUpEventData: eventId $eventId")

        val eventRef = mDatabase.collection("Events").document(eventId)
        eventRef.addSnapshotListener { value, _ ->

            value.let {

                val eventName = value?.get("event_name").toString()
                Log.d("TAG", "setUpEventData: eventName $eventName")


                //show event name
                Toast.makeText(this, "${value?.get("event_name")}", Toast.LENGTH_SHORT).show()
//                Snackbar.make(root_layout_event,"${value?.get("event_name")}", Snackbar.LENGTH_SHORT)
//                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
//                    .show()


                //get event data from firestore
                //Must be same name as firestore
                val event_type = value?.get("event_type").toString()
                val event_name = value?.get("event_name").toString()
                val event_date = value?.get("event_date").toString()
                val event_location = value?.get("event_location").toString()
                val event_meet = value?.get("event_meet").toString()
                val event_time = value?.get("event_time").toString()
                val event_member = value?.get("event_member").toString()
                val event_creator = value?.get("event_creator").toString()

                //get creator's name
                Log.d("TAG", "setUpEventData: event_creator_id $event_creator")
                val userRef = mDatabase.collection("Users").document(event_creator)
                userRef.addSnapshotListener { value, _ ->

                    value.let {

                        val creatorName = value?.get("display_name").toString()
                        Log.d("TAG", "setUpEventData: creatorName $creatorName")


                        //set event data to view
                        tv_event_review_type.text = "Type : $event_type"
                        tv_event_review_name.text = event_name
                        tv_event_review_date.text = event_date
                        event_review_location.text = event_location
                        event_review_meet.text = event_meet
                        event_review_time.text = event_time
                        event_review_member.text = event_member
                        tv_creator_review.text = creatorName

                        //show member list
                        showAllMember(eventId)
                    }
                }
            }


        }

    }


    @SuppressLint("SetTextI18n")
    private fun showAllMember(eventId: String) {

//        Log.d("eventId", eventId)
//        Log.d("eventName", eventName)
        memberReference = mDatabase.collection("Event-mem-list").document(eventId)
        memberReference.addSnapshotListener { value, _ ->

            value.let {

                value?.data?.values.apply {
//                    Log.d("event-list-mem", this.toString())

                    //Todo : show member list -> completed !!
                    this?.forEachIndexed { index, value ->
//                    Log.d("event-loop", "$index : $value")
                        edt_list_name.editableText?.append("${index + 1}. $value \n")
                    }
                }
            }

        }
    }


    private fun topAppBarAction(eventId: String) {

        Log.d("TAG", "topAppBarAction: eventId $eventId")

        // <-- Back btn
        topAppBar.setNavigationOnClickListener {
            finish()
        }

        // setting btn
        topAppBar.setOnMenuItemClickListener {

            when (it.itemId) {

                R.id.feedback -> {

                    val intent = Intent(this, SendFeedbackActivity::class.java)
                    startActivity(intent)
                    finish()

                    true
                }

                else -> false

            }
        }
    }


}