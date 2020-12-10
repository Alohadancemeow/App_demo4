package com.example.app_demo4.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.app_demo4.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_create_event1.*

class CreateEvent1Activity : AppCompatActivity() {

    // Firebase Properties
    private lateinit var mDatabase : FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

    // View Properties
    private lateinit var eventName : String
    private lateinit var eventLocation : String
    private lateinit var eventDate : String
    private lateinit var eventTimeStart : String
    private lateinit var eventTimeEnd : String
    private lateinit var eventMeet : String
    private lateinit var eventMember : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_create_event1)

        //set firebase properties
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        // <-- Back
        btn_back_event_create.setOnClickListener {
            finish()
        }

        btn_create_event.setOnClickListener {

            eventName = tv_event_name_create.editText?.text.toString().trim()
            eventLocation = tv_location_create.editText?.text.toString().trim()
            eventDate = tv_date_create.editText?.text.toString().trim()
            eventTimeStart = tv_time_start_create.editText?.text.toString().trim()
            eventTimeEnd = tv_time_end_create.editText?.text.toString().trim()
            eventMeet = tv_meet_create.editText?.text.toString().trim()
            eventMember = tv_member_create.editText?.text.toString().trim()

            createEvent(eventName,eventLocation, eventDate, eventTimeStart,eventTimeEnd,eventMeet, eventMember)
        }

    }

    /** Functions here */
    private fun createEvent(
        eventName: String,
        eventLocation: String,
        eventDate: String,
        eventTimeStart: String,
        eventTimeEnd: String,
        eventMeet: String,
        eventMember: String
    ) {

        if (!validateEventData(eventName,eventLocation,eventDate,eventTimeStart,eventTimeEnd,eventMeet,eventMember))
            return

        //else
        sendEventDataToFirebase(eventName,eventLocation,eventDate,eventTimeStart,eventTimeEnd,eventMeet,eventMember)

    }

    private fun sendEventDataToFirebase(
        eventName: String,
        eventLocation: String,
        eventDate: String,
        eventTimeStart: String,
        eventTimeEnd: String,
        eventMeet: String,
        eventMember: String
    ) {

        val user = mAuth.currentUser
        val userId = user!!.uid
        val eventRef = mDatabase.collection("Events").document(userId)
        val eventObject = HashMap<String, String>().apply {
            this["event_name"] = eventName
            this["event_location"] = eventLocation
            this["event_date"] = eventDate
            this["event_time_start"] = eventTimeStart
            this["event_time_end"] = eventTimeEnd
            this["event_meet"] = eventMeet
            this["event_member"] = eventMember
        }

        eventRef.set(eventObject).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, "Create event successful", Toast.LENGTH_SHORT).show()
//                finish()
            }
            else {
                Toast.makeText(this, "Create event unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }

    }


    /** validations */
    private fun validateEventData(
        eventName: String,
        eventLocation: String,
        eventDate: String,
        eventTimeStart: String,
        eventTimeEnd: String,
        eventMeet: String,
        eventMember: String
    ): Boolean {
        return when{
            eventName.isEmpty() ->{
                tv_event_name_create.error = "Field can not be empty"
                false
            }
            eventLocation.isEmpty() ->{
                tv_location_create.error = "Field can not be empty"
                false
            }
            eventDate.isEmpty() ->{
                tv_date_create.error = "Field can not be empty"
                false
            }
            eventTimeStart.isEmpty() ->{
                tv_time_start_create.error = "Field can not be empty"
                false
            }
            eventTimeEnd.isEmpty() ->{
                tv_time_end_create.error = "Field can not be empty"
                false
            }
            eventMeet.isEmpty() ->{
                tv_meet_create.error = "Field can not be empty"
                false
            }
            eventMember.isEmpty() ->{
                tv_member_create.error = "Field can not be empty"
                false
            }
            else -> {
                tv_event_name_create.error = null
                tv_location_create.error = null
                tv_date_create.error = null
                tv_time_start_create.error = null
                tv_time_end_create.error = null
                tv_meet_create.error = null
                tv_member_create.error = null
                true
            }
        }
    }

}