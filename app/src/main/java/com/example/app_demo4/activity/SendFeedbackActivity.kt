package com.example.app_demo4.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.app_demo4.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_event1.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_send_feedback.*

class SendFeedbackActivity : AppCompatActivity() {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    // View Properties
    private lateinit var subject: String
    private lateinit var details: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_send_feedback)

        //set firebase
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()


        //<- Back
        iv_cancel.setOnClickListener {
            finish()
        }

        //-> Send feedback
        btn_send_feedback.setOnClickListener {

            val userId = mAuth.currentUser!!.uid
            val userRef = mDatabase.collection("Users").document(userId)
            userRef.addSnapshotListener { value, error ->

                error.let {

                    //get username
                    val userName = value?.get("display_name").toString()

                    //set view
                    subject = edt_subject.editText?.text.toString().trim()
                    details = edt_details.editText?.text.toString().trim()

                    if (!validate(subject,details)) return@addSnapshotListener

                    //else
                    sendFeedbackToFirebase(userName, subject, details)

                }
            }


        }
    }

    private fun validate(subject: String, details: String): Boolean {

        return when {
            subject.isEmpty() -> {
                edt_subject.error = "Field cannot be empty"
                false
            }
            details.isEmpty() -> {
                edt_details.error = "Field cannot be empty"
                false
            }
            else -> {
                edt_subject.error = null
                edt_details.error = null
                true
            }
        }

    }

    private fun sendFeedbackToFirebase(userName: String, subject: String, details: String) {

        val feedbackRef = mDatabase.collection("Feedback")
        val feedbackObject = HashMap<String, String>().apply {
            this["sender"] = userName
            this["subject"] = subject
            this["details"] = details
        }

        feedbackRef.add(feedbackObject).addOnCompleteListener {

            if (it.isSuccessful) {

                edt_subject.editText?.text = null
                edt_details.editText?.text = null

                Snackbar.make(root_layout,"Thanks for feedback", Snackbar.LENGTH_SHORT).show()
//                finish()
            } else {
                Toast.makeText(this, "send failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}