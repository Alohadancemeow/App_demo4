package com.example.app_demo4.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.app_demo4.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_event_review.*
import kotlinx.android.synthetic.main.activity_profile_review.*


class EventReviewActivity : AppCompatActivity() {

    //Firebase Property
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var memberReference: DocumentReference

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_event_review)

        // set property
        mDatabase = FirebaseFirestore.getInstance()


        // <-- Button Back
        btn_back_event_review.setOnClickListener {
            finish()
        }

        // Button OK
        btn_event_review.setOnClickListener {
            finish()
        }

        // Receive intent from Event 1/2 Fragments and HomeFragment
        if (intent.extras != null) {

            val eventId = intent.extras!!.get("eventId").toString()
            val eventRef = mDatabase.collection("Events").document(eventId)

            eventRef.addSnapshotListener { value, error ->

                if (error != null) finish()

                val eventName = value?.get("event_name").toString()

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


                //set event data to view
                tv_event_review_type.text = "$event_type Event"
                tv_event_review_name.text = event_name
                tv_event_review_date.text = event_date
                event_review_location.editText?.setText(event_location)
                event_review_meet.editText?.setText(event_meet)
                event_review_time.editText?.setText(event_time)
                event_review_member.editText?.setText(event_member)
                tv_creator_review.text = "Creator : $event_creator"

                showAllMember(eventId, eventName)
            }
        }

    }


    @SuppressLint("SetTextI18n")
    private fun showAllMember(eventId: String, eventName: String) {

//        Log.d("eventId", eventId)
//        Log.d("eventName", eventName)
        memberReference = mDatabase.collection("Event-mem-list").document(eventName)
        memberReference.addSnapshotListener { value, error ->

            error.let {

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
}