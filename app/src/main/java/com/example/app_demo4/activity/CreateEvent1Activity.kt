package com.example.app_demo4.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.app_demo4.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_event1.*
import kotlinx.android.synthetic.main.activity_event_review.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes

class CreateEvent1Activity : AppCompatActivity() {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

    // View Properties
    private lateinit var eventName: String
    private lateinit var eventLocation: String
    private lateinit var eventDate: String
    private lateinit var eventTime: String
    private lateinit var eventMeet: String
    private lateinit var eventMember: String
    private lateinit var eventCreator: String

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

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_create_event1)

        //set firebase properties
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        // show event type
        Snackbar.make(root_layout_event_a,"General Event", Snackbar.LENGTH_LONG)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setAction("Back") {
                finish()
            }
            .show()

//        pickDateTime()

        // <-- Back
        btn_back_event_create.setOnClickListener {
            finish()
        }




        //new date
        getDate()

        //new
        getTime{ timeInMillis ->
            setEvent(timeInMillis)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun getDate() {
        iv_date_pick.setOnClickListener {

            Calendar.getInstance().apply {
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)

                //get date
                DatePickerDialog(
                    this@CreateEvent1Activity,
                    0,
                    { _, year, month, day ->
                        this.set(Calendar.YEAR, year)
                        this.set(Calendar.MONTH, month)
                        this.set(Calendar.DAY_OF_MONTH, day)


                        //set date to edit text
                        savedDay = day
                        savedMonth = month +1 // month start at 0
                        savedYear = year
                        tv_date_create.editText?.setText("$savedDay/$savedMonth/$savedYear")

                    },
                    this.get(Calendar.YEAR),
                    this.get(Calendar.MONTH),
                    this.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        }
    }


    //get timeInMillis
    @SuppressLint("SetTextI18n")
    @ExperimentalTime
   fun getTime(callback: (Long) -> Unit) {

        iv_time_pick.setOnClickListener {
            Calendar.getInstance().apply {
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)

                TimePickerDialog(
                    this@CreateEvent1Activity,
                    0,
                    { _, hour, minute  ->

                        this.set(Calendar.HOUR_OF_DAY, hour)
                        this.set(Calendar.MINUTE, minute)


                        val str = "$hour:$minute"
                        val str2 = hour.hours.inMilliseconds.toLong()
                        val str3 = minute.minutes.inMilliseconds.toLong()
                        val str4 = str2 + str3

                        Toast.makeText(this@CreateEvent1Activity, str, Toast.LENGTH_SHORT).show()

                        Log.d("time", str2.toString())
                        Log.d("time2", str3.toString())
                        Log.d("time4", str4.toString())

                        //set time to edit text
                        savedHour = hour
                        savedMinute = minute
                        tv_time_create.editText?.setText("$savedHour : $savedMinute")


                        callback(this.timeInMillis) //return

                    },
                    this.get(Calendar.HOUR_OF_DAY),
                    this.get(Calendar.MINUTE),
                    true  // true = 24 hour, false = AM-PM
                ).show()

            }
        }
    }

    private fun setEvent(timeInMillis: Long) {

        Log.d("time-it",timeInMillis.toString())


        // Button Create event
        btn_create_event.setOnClickListener {

            val userId = mAuth.currentUser!!.uid
            val userRef = mDatabase.collection("Users").document(userId)
            userRef.addSnapshotListener { value, error ->

                error.let {

                    //get username
                    val userName = value?.get("display_name").toString()

                    //set view properties
                    eventName = tv_event_name_create.editText?.text.toString().trim()
                    eventLocation = tv_location_create.editText?.text.toString().trim()
                    eventDate = tv_date_create.editText?.text.toString().trim()
                    eventTime = tv_time_create.editText?.text.toString().trim()
                    eventMeet = tv_meet_create.editText?.text.toString().trim()
                    eventMember = tv_member_create.editText?.text.toString().trim()

                    eventCreator = userName

                    createEvent(
                        eventName,
                        eventLocation,
                        eventDate,
                        eventTime,
                        eventMeet,
                        eventMember,
                        eventCreator,
                        timeInMillis
                    )

                }
            }


        }
    }

    /** Functions here */

//    private fun getDateTimeCalendar() {
//
//        val cal = Calendar.getInstance()
//
//        //set current datetime
//        day = cal.get(Calendar.DAY_OF_MONTH)
//        month = cal.get(Calendar.MONTH)
//        year = cal.get(Calendar.YEAR)
//        hour = cal.get(Calendar.HOUR)
//        minute = cal.get(Calendar.MINUTE)
//
//    }

//    private fun pickDateTime() {
//
//        //icon pick date
//        iv_date_pick.setOnClickListener {
//            getDateTimeCalendar()
//            DatePickerDialog(this, this, year, month, day).show()
//        }
//
//        //icon pick time
//        iv_time_pick.setOnClickListener {
//            getDateTimeCalendar()
//            TimePickerDialog(this, this, hour, minute, true).show()
//            // true = 24 hour, false = AM-PM
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//
//        savedDay = dayOfMonth
//        savedMonth = month +1 // month start at 0
//        savedYear = year
//        tv_date_create.editText?.setText("$savedDay/$savedMonth/$savedYear")
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
//
//        savedHour = hourOfDay
//        savedMinute = minute
//        tv_time_create.editText?.setText("$savedHour : $savedMinute")
//    }


    private fun createEvent(
        eventName: String,
        eventLocation: String,
        eventDate: String,
        eventTime: String,
        eventMeet: String,
        eventMember: String,
        eventCreator: String,
        timeInMillis: Long
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
            eventMember,
            eventCreator,
            timeInMillis
        )

    }

    private fun sendEventDataToFirebase(
        eventName: String,
        eventLocation: String,
        eventDate: String,
        eventTime: String,
        eventMeet: String,
        eventMember: String,
        eventCreator: String,
        timeInMillis: Long
    ) {

//        val user = mAuth.currentUser
//        val userId = user!!.uid

        val eventRef = mDatabase.collection("Events")
        val eventObject = HashMap<String, String>().apply {
            this["event_name"] = eventName
            this["event_location"] = eventLocation
            this["event_date"] = eventDate
            this["event_time"] = eventTime
            this["event_meet"] = eventMeet
            this["event_member"] = eventMember
            this["event_type"] = "General"  //type A
            this["event_creator"] = eventCreator  //creator name
            this["timeInMillis"] = timeInMillis.toString()
        }

        // #use add() to collection, #use set() to document
        // -> การเขียนข้อมูลด้วย Method add()
        // -> ทาง Cloud FireStore จะสร้าง Index ให้เราโดยอัตโนมัติ
        eventRef.add(eventObject).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Create event successful", Toast.LENGTH_SHORT).show()

                // todo -> than send to event1 (A) page
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
                tv_event_name_create.error = "Field cannot be empty"
                false
            }
            eventLocation.isEmpty() -> {
                tv_location_create.error = "Field cannot be empty"
                false
            }
            eventDate.isEmpty() -> {
                tv_date_create.error = "Field cannot be empty"
                false
            }
            eventTime.isEmpty() -> {
                tv_time_create.error = "Field cannot be empty"
                false
            }
            eventMeet.isEmpty() -> {
                tv_meet_create.error = "Field cannot be empty"
                false
            }
            eventMember.isEmpty() -> {
                tv_member_create.error = "Field cannot be empty"
                false
            }
            else -> {
                tv_event_name_create.error = null
                tv_location_create.error = null
                tv_date_create.error = null
                tv_time_create.error = null
                tv_meet_create.error = null
                tv_member_create.error = null
                true
            }
        }
    }

}