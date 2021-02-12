package com.example.app_demo4.activity

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import com.example.app_demo4.R
import com.example.app_demo4.notification.ReminderBroadcast
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_event1.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.time.ExperimentalTime

class CreateEvent1Activity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userId: String

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
//    private var savedDay = 0
//    private var savedMonth = 0
//    private var savedYear = 0
//    private var savedHour = 0
//    private var savedMinute = 0

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_create_event1)

        //set firebase properties
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        userId = mAuth.currentUser!!.uid


        // show event type
        Snackbar.make(root_layout_event_a, "General Event", Snackbar.LENGTH_LONG)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setAction("Back") {
                finish()
            }
            .show()


        // <-- Back
        iv_cancel_event_create.setOnClickListener {
            finish()
        }

        // Select date and time
        pickDateTime {
//            startAlarm(it)
            clickToCreate(it)


        }

        // Button Create event
//        btn_create_event.setOnClickListener {
//
//            val userId = mAuth.currentUser!!.uid
//            val userRef = mDatabase.collection("Users").document(userId)
//            userRef.addSnapshotListener { value, error ->
//
//                error.let {
//
//                    //get username
//                    val userName = value?.get("display_name").toString()
//
//                    //set view properties
//                    eventName = tv_event_name_create.editText?.text.toString().trim()
//                    eventLocation = tv_location_create.text.toString().trim()
//                    eventDate = tv_date_create.text.toString().trim()
//                    eventTime = tv_time_create.text.toString().trim()
//                    eventMeet = tv_meet_create.text.toString().trim()
//                    eventMember = tv_member_create.text.toString().trim()
//
//                    eventCreator = userName
//
//                    createEvent(
//                        eventName,
//                        eventLocation,
//                        eventDate,
//                        eventTime,
//                        eventMeet,
//                        eventMember,
//                        eventCreator
//                    )
//
//                }
//            }
//
//
//        }
    }

    private fun clickToCreate(timeInMillis: Long) {

        // Button Create event
        btn_create_event.setOnClickListener {

            val userRef = mDatabase.collection("Users").document(userId)
            userRef.addSnapshotListener { value, error ->

                value.let {

                    //get username for event creator
                    val userName = value?.get("display_name").toString()

                    //set view properties
                    eventName = tv_event_name_create.editText?.text.toString().trim()
                    eventLocation = tv_location_create.text.toString().trim()
                    eventDate = tv_date_create.text.toString().trim()
                    eventTime = tv_time_create.text.toString().trim()
                    eventMeet = tv_meet_create.text.toString().trim()
                    eventMember = tv_member_create.text.toString().trim()

                    eventCreator = userName

                    createEvent(eventName, eventLocation, eventDate, eventTime, eventMeet, eventMember, eventCreator, timeInMillis)



                }
            }


        }

    }


    /** # Start DateTime Section */

    private fun getDateTimeCalendar() {

        val cal = Calendar.getInstance()

        //set current datetime
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

    }

    private fun pickDateTime(callback: (Long) -> Unit)  {

        //icon pick date
        tv_date_create.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()

        }

        //icon pick time
        tv_time_create.setOnClickListener {
//            getDateTimeCalendar()
//            TimePickerDialog(this, this, hour, minute, true).show()
            // true = 24 hour, false = AM-PM

            //new
            Calendar.getInstance().apply {

                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)

                //get time
                TimePickerDialog(
                    this@CreateEvent1Activity,
                    0,
                    { _, hour, min ->
                        this.set(Calendar.HOUR_OF_DAY, hour)
                        this.set(Calendar.MINUTE, min)

//                        formatTime(cal)
                        val timeTxt = DateFormat.getTimeInstance(DateFormat.SHORT).format(this.time)
                        tv_time_create.text = timeTxt

                        callback(this.timeInMillis) //use callback
                    },
                    this.get(Calendar.HOUR_OF_DAY),
                    this.get(Calendar.MINUTE),
                    true
                ).show()

            }

        }
    }

    //set date
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        formatDate(cal)
    }

    private fun formatDate(cal: Calendar) {
        val dateTxt = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.time)
        tv_date_create.text = dateTxt
    }

    //set time
//    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
//
//        val cal = Calendar.getInstance()
//        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
//        cal.set(Calendar.MINUTE, minute)
//        cal.set(Calendar.SECOND, 0)
//
//        formatTime(cal)
//    }
//
//    private fun formatTime(cal: Calendar) {
//        val timeTxt = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.time)
//        tv_time_create.text = timeTxt
//    }

    /** # End DateTime Section */


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
        }

        // #use add() to collection, #use set() to document
        // -> การเขียนข้อมูลด้วย Method add()
        // -> ทาง Cloud FireStore จะสร้าง Index ให้เราโดยอัตโนมัติ
        eventRef.add(eventObject).addOnSuccessListener {

            //new
            Toast.makeText(this, "Create event successful", Toast.LENGTH_SHORT).show()

            //get eventId
            val eventId = it.id
            Log.d("TAG", "sendEventDataToFirebase: ID $eventId")

            //startAlarm
            startAlarm(timeInMillis, eventId)

            finish()

        }

    }

    private fun startAlarm(timeInMillis: Long, eventId: String) {

//        Log.d("TAG", "startAlarm:eventId $eventId")
        Log.d("TAG", "startAlarm:timeInMillis $timeInMillis")
        Log.d("TAG", "startAlarm:eventId $eventId")


        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, ReminderBroadcast::class.java).apply {
            this.putExtra("eventId", eventId)
        }

        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)


        //check that current user is in ?
        val eventMemListRef = mDatabase.collection("Event-mem-list").document(eventName)
        eventMemListRef.addSnapshotListener { value, _ ->

            value.let {

                val memId = value?.data?.keys
                Log.d("TAG", "startAlarm: memIdOf($eventName) $memId")

                if (memId?.contains(userId) == true) {
                    Log.d("TAG", "startAlarm: true")

                    //set alarm
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                    )

                } else {
                    Log.d("TAG", "startAlarm: else")
                }
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