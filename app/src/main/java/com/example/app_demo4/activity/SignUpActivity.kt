package com.example.app_demo4.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Button
import android.widget.Toast
import com.example.app_demo4.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    // Firebase Properties
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    // View Properties
    private lateinit var reqDisplayName: String
    private lateinit var reqFullName: String
    private lateinit var reqEmail: String
    private lateinit var reqPhone: String
    private lateinit var reqPassword: String
    private lateinit var signUpButton: Button

    /** Main Here */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(FLAG_FULLSCREEN)
        setContentView(R.layout.activity_sign_up)

        //set firebase properties
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()


        // # Button Sign up
        signUpButton = btn_sign_up_sign_up.apply {

            setOnClickListener {

                //set view properties
                reqDisplayName = req_display_name.editText?.text.toString().trim()
                reqFullName = req_full_name.editText?.text.toString().trim()
                reqEmail = req_email.editText?.text.toString().trim()
                reqPhone = req_phone.editText?.text.toString().trim()
                reqPassword = req_password.editText?.text.toString().trim()

                createUser(reqDisplayName, reqFullName, reqEmail, reqPhone, reqPassword)

            }
        }
    }

    /** Functions Here */

    // # Create user
    private fun createUser(reqDisplayName: String, reqFullName: String, reqEmail: String, reqPhone: String, reqPassword: String) {

        /** # Check sign up --> validation -ส่งไปพิสูจน์อักษร */
        if (!validateDisplayName(reqDisplayName) || !validateFullName(reqFullName) ||
            !validateEmail(reqEmail) || !validatePhone(reqPhone) || !validatePassword(reqPassword)
        ) {
            return
        }
        else {
            /** # เมื่อกรอกข้อมูลถูกต้องตามที่ validate ให้ทำการ create user */
            mAuth.createUserWithEmailAndPassword(reqEmail, reqPassword)
                .addOnCompleteListener { it ->

                    if (it.isSuccessful) {
                        sendUserdataToFirebase(reqDisplayName, reqFullName, reqEmail, reqPhone, reqPassword)
                    }
                }
        }
    }

    // # Send user data to firebase
    private fun sendUserdataToFirebase(reqDisplayName: String, reqFullName: String, reqEmail: String, reqPhone: String, reqPassword: String) {

        val user = mAuth.currentUser
        val userId = user!!.uid
        val userRef = mDatabase.collection("Users").document(userId)

        /** # Write Database Objects -ข้อมูลที่ต้องการจะส่งไปเก็บที่ database */
        val userObject = HashMap<String, String>().apply {
            this["UID"] = userId
            this["display_name"] = reqDisplayName
            this["full_name"] = reqFullName
            this["email"] = reqEmail
            this["phone"] = reqPhone
            this["password"] = reqPassword
            this["wat"] = "Chana Songkram"
            this["kana"] = "12"
            this["status"] = ""
            this["age"] = ""
        }

        /** # Let's go -พร้อมแล้วส่งก้อนข้อมูลไปโลด */
        userRef.set(userObject).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Create account successful", Toast.LENGTH_SHORT).show()

                /** # Send to profile -เมื่อสร้างสำเร็จให้ส่งไปหน้าโปรไฟล์ พร้อมกับ uid */
                val intent = Intent(this, ProfileSettingActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Create account unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /** # Sign up validation */
    private fun validateDisplayName(reqDisplayName: String): Boolean {
//        val valueOfDisplayName: String = req_display_name.editText?.text.toString()

        return when {
            reqDisplayName.isEmpty() -> {
                req_display_name.error = "Field cannot be empty"
                false
            }
            reqDisplayName.length >= 20 -> {
                req_display_name.error = "Displayname too long"
                false
            }
            else -> {
                req_display_name.error = null
//                req_display_name.isErrorEnabled = false
                true
            }
        }
    }

    private fun validateFullName(reqFullName: String): Boolean {
//        val valueOfFullName: String = req_full_name.editText?.text.toString()

        return if (reqFullName.isEmpty()) {
            req_full_name.error = "Field cannot be empty"
            false
        } else {
            req_full_name.error = null
            true
        }
    }

    private fun validateEmail(reqEmail: String): Boolean {
//        val valueOfEmail: String = req_email.editText?.text.toString()
//        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return when {
            reqEmail.isEmpty() -> {
                req_email.error = "Field cannot be empty"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(reqEmail).matches() -> {
                req_email.error = "Invalid email address"
                false
            }
            else -> {
                req_email.error = null
                true
            }
        }
    }

    private fun validatePhone(reqPhone: String): Boolean {
//        val valueOfPhone: String = req_phone.editText?.text.toString()

        return when {
            reqPhone.isEmpty() -> {
                req_phone.error = "Field cannot be empty"
                false
            }
            reqPhone.length != 10 -> {
                req_phone.error = "Invalid phone number"
                false
            }
            else -> {
                req_phone.error = null
                true
            }
        }
    }

    private fun validatePassword(reqPassword: String): Boolean {
//        val valueOfPassword: String = req_password.editText?.text.toString()

        return when {
            reqPassword.isEmpty() -> {
                req_password.error = "Field cannot be empty"
                false
            }
            reqPassword.length < 6 -> {
                req_password.error = "Password too weak"
                false
            }
            else -> {
                req_password.error = null
                true
            }
        }
    }

}