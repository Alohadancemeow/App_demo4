package com.example.app_demo4.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.app_demo4.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_review.*

class ProfileReviewActivity : AppCompatActivity() {

    // Firebase Property
    private lateinit var mDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_profile_review)

        // set property
        mDatabase = FirebaseFirestore.getInstance()

        // Receive intent from User 1/2 Fragments
        if (intent.extras != null) {

            val userId = intent.extras!!.get("userId").toString()
            val userRef = mDatabase.collection("Users").document(userId)
            userRef.addSnapshotListener { value, error ->

                if (error != null) finish()

                Toast.makeText(baseContext, "User id: $userId", Toast.LENGTH_LONG).show()

                // else
                val display_name = value?.get("display_name").toString()
                val full_name = value?.get("full_name").toString()
                val status = value?.get("status").toString()
                val wat = value?.get("wat").toString()
                val kana = value?.get("kana").toString()
                val age = value?.get("age").toString()
                val email = value?.get("email").toString()
                val phone = value?.get("phone").toString()

                tv_display_name_review.text = display_name
                tv_full_name_review.text = full_name
                status_review.editText?.setText(status)
                temple_review.editText?.setText(wat)
                kana_review.editText?.setText(kana)
                age_review.editText?.setText(age)
                email_review.editText?.setText(email)
                phone_review.editText?.setText(phone)
            }
        }

        // <- Back button
        btn_back_profile.setOnClickListener {
            finish()
        }

        // Call button
        btn_profile_call.setOnClickListener {
            TODO("phone call")
        }
    }

}