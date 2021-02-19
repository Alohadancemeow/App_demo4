package com.example.app_demo4.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.app_demo4.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_review.*

class ProfileReviewActivity : AppCompatActivity() {

    // Firebase Property
    private lateinit var mDatabase: FirebaseFirestore

    //Top Appbar
    private lateinit var topAppBar: MaterialToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_profile_review)

        // set property
        mDatabase = FirebaseFirestore.getInstance()

        // set Top appbar
        topAppBar = top_AppBar_profile_preview.apply {
            topAppBarAction(this)
        }




        // Receive intent from User 1/2 Fragments
        intent.let {

            val userId = intent.extras!!.get("userId").toString()
            val userRef = mDatabase.collection("Users").document(userId)
            userRef.addSnapshotListener { value, error ->

                value.let {

                    //show user's display name
//                Toast.makeText(baseContext, "${value?.get("display_name")}", Toast.LENGTH_LONG).show()
                    Snackbar.make(root_layout_profile,"${value?.get("display_name")}", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .show()

                    //get user's data from firebase
                    val display_name = value?.get("display_name").toString().trim()
                    val full_name = value?.get("full_name").toString().trim()
                    val status = value?.get("status").toString().trim()
                    val wat = value?.get("wat").toString().trim()
                    val kana = value?.get("kana").toString().trim()
                    val age = value?.get("age").toString().trim()
                    val email = value?.get("email").toString().trim()
                    val phone = value?.get("phone").toString().trim()

                    //then set it(data) to view
                    tv_display_name_review.text = display_name
                    tv_full_name_review.text = full_name
                    status_review.editText?.setText(status)
                    temple_review.editText?.setText(wat)
                    kana_review.editText?.setText(kana)
                    age_review.editText?.setText(age)
                    email_review.editText?.setText(email)
                    phone_review.editText?.setText(phone)

                    // Call Now button
                    btn_profile_call.setOnClickListener {
                        startCall(phone)
                    }

                }
            }
        }

    }

    private fun topAppBarAction(materialToolbar: MaterialToolbar) {

        // <-- Back btn
        materialToolbar.setNavigationOnClickListener {
            finish()
        }

        // setting btn
        materialToolbar.setOnMenuItemClickListener {

            when (it.itemId) {

                R.id.feedback -> {

                    val intent = Intent(this, SendFeedbackActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false

            }
        }
    }

    private fun startCall(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: $phone")

        if (intent.resolveActivity(packageManager) != null)  // หรือไม่ใช้ก็ได้ ?
            startActivity(intent)

    }

}