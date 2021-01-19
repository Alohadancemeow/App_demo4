//package com.example.app_demo4.notification
//
//import android.app.DatePickerDialog
//import android.app.TimePickerDialog
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import com.example.app_demo4.R
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.CollectionReference
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.MetadataChanges
//import kotlinx.android.synthetic.main.activity_notification_main.*
//import java.util.*
//
//class NotificationMainActivity : AppCompatActivity() {
//
//    lateinit var alarmService: AlarmService
//
//    private lateinit var mDatabase: FirebaseFirestore
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var userId: String
//
//
//    // Date Properties
//    private var day = 0
//    private var month = 0
//    private var year = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        setContentView(R.layout.activity_notification_main)
//
//        mDatabase = FirebaseFirestore.getInstance()
//        mAuth = FirebaseAuth.getInstance()
//        userId = mAuth.currentUser!!.uid
//
////        getUserId()
//
//        getEventId()
//
//
//        alarmService = AlarmService(this)
//
//        button.setOnClickListener {
//            setAlarm { timeInMillis -> alarmService.setExactAlarm(timeInMillis)
//            Log.d("it1", timeInMillis.toString())
//            }
//        }
//
//        button2.setOnClickListener {
//            setAlarm { alarmService.setRepetitiveAlarm(it)
//                Log.d("it2", it.toString())
//            }
//        }
//
//    }
//
//    private fun getEventId() {
//
//        getToday()
//        val today = "$day/${month + 1}/$year"
//
//        val eventRef = mDatabase.collection("Events")
//            .whereEqualTo("event_date", today).orderBy("event_time")
//
//        eventRef.addSnapshotListener { value, error ->
//            error.let {
//
//                value?.forEach {
////                    val e = it.data.values
////                    Log.d("event-e", e.toString())
//
//                    val eventId = it.id
//                    Log.d("event-id", it.id)
//
//                    getEventName(eventId)
//                }
//
//            }
//        }
//    }
//
//    private fun getEventName(eventId: String) {
//        val eventRef = mDatabase.collection("Events").document(eventId)
//        eventRef.addSnapshotListener { value, error ->
//
//            error.let {
//                val eventName = value?.data?.get("event_name").toString()
//                val eventTime = value?.data?.get("event_time").toString()
//                Log.d("event-name","$eventName + $eventTime")
//
//                getEventMemList(eventName,eventTime)
//            }
//        }
//    }
//
//    private fun getEventMemList(eventName: String, eventTime: String) {
//        Log.d("userId",userId)
//        Log.d("userName",eventName)
//        Log.d("userTime",eventTime)
//
//        val eventMemListRef = mDatabase.collection("Event-mem-list").document(eventName)
//        eventMemListRef.addSnapshotListener { value, error ->
//
//            error.let {
//               val memId = value?.data?.keys
//                Log.d("user-memId", memId.toString())
//
//                if (memId?.contains(userId) == true) {
////                    alarmService.setExactAlarm(eventTime)
//                }
//            }
//        }
//    }
//
//    private fun getToday() {
//        val cal = Calendar.getInstance()
//        day = cal.get(Calendar.DAY_OF_MONTH)
//        month = cal.get(Calendar.MONTH)
//        year = cal.get(Calendar.YEAR)
//    }
//
//
//
//
//    // Alarm
//    private fun setAlarm(callback: (Long) -> Unit) {
//        Calendar.getInstance().apply {
//            this.set(Calendar.SECOND, 0)
//            this.set(Calendar.MILLISECOND, 0)
//
//            //get date
//            DatePickerDialog(
//                this@NotificationMainActivity,
//                0,
//                { _, year, month, day ->
//                    this.set(Calendar.YEAR, year)
//                    this.set(Calendar.MONTH, month)
//                    this.set(Calendar.DAY_OF_MONTH, day)
//
//                    //get time
//                    TimePickerDialog(
//                        this@NotificationMainActivity,
//                        0,
//                        { _, hour, min ->
//                            this.set(Calendar.HOUR_OF_DAY, hour)
//                            this.set(Calendar.MINUTE, min)
//                            callback(this.timeInMillis)
//                        },
//                        this.get(Calendar.HOUR_OF_DAY),
//                        this.get(Calendar.MINUTE),
//                        false
//                    ).show()
//
//                },
//                this.get(Calendar.YEAR),
//                this.get(Calendar.MONTH),
//                this.get(Calendar.DAY_OF_MONTH)
//            ).show()
//        }
//    }
//}