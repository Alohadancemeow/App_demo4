package com.example.app_demo4.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.app_demo4.R
import com.example.app_demo4.model.ProgressButton
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_your_name.*
import kotlinx.android.synthetic.main.progress_btn_layout.*

class YourNameActivity : AppCompatActivity() {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userId: String

    private lateinit var progressBarYourName: View

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_your_name)

        //set firebase properties
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseFirestore.getInstance()
        userId = mAuth.currentUser!!.uid

        getCurrentUser()

        //set text and icon on Button
        val textViewButton: MaterialButton = textView_progress
        textViewButton.apply {
            this.text = "Save"
            this.setIconResource(R.drawable.ic_check_white)
        }

        progressBarYourName = your_name_progressbar_button.apply {

            setOnClickListener {

                //call progressbar
                val progressButton = ProgressButton(it)
                progressButton.buttonActivated(4)

                //delay
                val handler = Handler()
                handler.postDelayed({

                    //save
                    updateData(userId)

                    //end progressbar
                    progressButton.buttonFinished(4)

                }, 2000)

            }
        }


        // <- Back button
        btn_back_edit.setOnClickListener {
            finish()
        }
    }


    private fun getCurrentUser() {
        val userRef = mDatabase.collection("Users").document(userId)
        userRef.addSnapshotListener { value, error ->

            if (error != null) finish()

            // else
            val displayNameFromDB = value?.get("display_name").toString()
            val fullNameFromDB = value?.get("full_name").toString()

            tv_edit_display_name.editText?.setText(displayNameFromDB)
            tv_edit_full_name.editText?.setText(fullNameFromDB)
        }
    }

    private fun updateData(userId: String) {

        val newDisplayName = tv_edit_display_name.editText?.text.toString().trim()
        val newFullName = tv_edit_full_name.editText?.text.toString().trim()

        validateData(newDisplayName, newFullName, userId)
    }

    private fun validateData(newDisplayName: String, newFullName: String, userId: String) {

        if (!validateDisplayName(newDisplayName) || !validateFullName(newFullName)) return

        // else
        val newData = HashMap<String, Any>().apply {
            this["display_name"] = newDisplayName
            this["full_name"] = newFullName
        }

        /** -> อัพเดตข้อมูลขึ้น Firebase database */
        mDatabase.collection("Users").document(userId).update(newData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Your name is saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Your name is failed", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun validateDisplayName(reqDisplayName: String): Boolean {
//        val valueOfDisplayName: String = req_display_name.editText?.text.toString()

        return when {
            reqDisplayName.isEmpty() -> {
                tv_edit_display_name.error = "Field cannot be empty"
                false
            }
            reqDisplayName.length >= 20 -> {
                tv_edit_display_name.error = "Displayname too long"
                false
            }
            else -> {
                tv_edit_display_name.error = null
//                req_display_name.isErrorEnabled = false
                true
            }
        }
    }

    private fun validateFullName(reqFullName: String): Boolean {
//        val valueOfFullName: String = req_full_name.editText?.text.toString()

        return if (reqFullName.isEmpty()) {
            tv_edit_full_name.error = "Field cannot be empty"
            false
        } else {
            tv_edit_full_name.error = null
            true
        }
    }
}