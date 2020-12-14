package com.example.app_demo4.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.example.app_demo4.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_event2.*
import java.util.*
import kotlin.collections.HashMap

class CreateEvent2Activity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
//    private lateinit var mAuth : FirebaseAuth

    // View Properties
    private lateinit var eventName: String
    private lateinit var eventLocation: String
    private lateinit var eventDate: String
    private lateinit var eventTime: String
    private lateinit var eventMeet: String
    private lateinit var eventMember: String

    // DateTime Properties
    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    //saved
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var savedHour = 0
    private var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_create_event2)

        //set firebase properties
        mDatabase = FirebaseFirestore.getInstance()
//        mAuth = FirebaseAuth.getInstance()

        pickDateTime()

        // <-- Back
        btn_back_event_create2.setOnClickListener {
            finish()
        }

        // Button Create event
        btn_create_event2.setOnClickListener {

            //set view properties
            eventName = tv_event_name_create2.editText?.text.toString().trim()
            eventLocation = tv_location_create2.editText?.text.toString().trim()
            eventDate = tv_date_create2.editText?.text.toString().trim()
            eventTime = tv_time_create2.editText?.text.toString().trim()
            eventMeet = tv_meet_create2.editText?.text.toString().trim()
            eventMember = tv_member_create2.editText?.text.toString().trim()

            createEvent(
                eventName,
                eventLocation,
                eventDate,
                eventTime,
                eventMeet,
                eventMember
            )
        }

    }

    /** Functions here */

    private fun getDateTimeCalendar() {

        val cal = Calendar.getInstance()

        //set current datetime
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

    }

    private fun pickDateTime() {

        //icon pick date
        iv_date_pick2.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }

        //icon pick time
        iv_time_pick2.setOnClickListener {
            getDateTimeCalendar()
            TimePickerDialog(this, this, hour, minute, true).show()
            // true = 24 hour, false = AM-PM
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        savedDay = dayOfMonth
        savedMonth = month +1 // month start at 0
        savedYear = year
        tv_date_create2.editText?.setText("$savedDay/$savedMonth/$savedYear")
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {

        savedHour = hourOfDay
        savedMinute = minute
        tv_time_create2.editText?.setText("$savedHour : $savedMinute")
    }


    private fun createEvent(
        eventName: String,
        eventLocation: String,
        eventDate: String,
        eventTime: String,
        eventMeet: String,
        eventMember: String
    ) {

        if (!validateEventData(
                eventName,
                eventLocation,
                eventDate,
                eventTime,
                eventMeet,
                eventMember
            )
        )
            return

        //else
        sendEventDataToFirebase(
            eventName,
            eventLocation,
            eventDate,
            eventTime,
            eventMeet,
            eventMember
        )

    }

    private fun sendEventDataToFirebase(
        eventName: String,
        eventLocation: String,
        eventDate: String,
        eventTime: String,
        eventMeet: String,
        eventMember: String
    ) {

//        val user = mAuth.currentUser
//        val userId = user!!.uid

        val eventRef = mDatabase.collection("Events").document()
        val eventObject = HashMap<String, String>().apply {
            this["event_name"] = eventName
            this["event_location"] = eventLocation
            this["event_date"] = eventDate
            this["event_time"] = eventTime
            this["event_meet"] = eventMeet
            this["event_member"] = eventMember
            this["event_type"] = "Event B"  //type B
        }

        // #use add() to collection, #use set() to document
        // -> การเขียนข้อมูลด้วย Method add()
        // -> ทาง Cloud FireStore จะสร้าง Index ให้เราโดยอัตโนมัติ
        eventRef.set(eventObject).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Create event successful", Toast.LENGTH_SHORT).show()

                // todo -> than send to event1 (B) page
                finish()
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
        eventTime: String,
        eventMeet: String,
        eventMember: String
    ): Boolean {
        return when {
            eventName.isEmpty() -> {
                tv_event_name_create2.error = "Field cannot be empty"
                false
            }
            eventLocation.isEmpty() -> {
                tv_location_create2.error = "Field cannot be empty"
                false
            }
            eventDate.isEmpty() -> {
                tv_date_create2.error = "Field cannot be empty"
                false
            }
            eventTime.isEmpty() -> {
                tv_time_create2.error = "Field cannot be empty"
                false
            }
            eventMeet.isEmpty() -> {
                tv_meet_create2.error = "Field cannot be empty"
                false
            }
            eventMember.isEmpty() -> {
                tv_member_create2.error = "Field cannot be empty"
                false
            }
            else -> {
                tv_event_name_create2.error = null
                tv_location_create2.error = null
                tv_date_create2.error = null
                tv_time_create2.error = null
                tv_meet_create2.error = null
                tv_member_create2.error = null
                true
            }
        }
    }

}