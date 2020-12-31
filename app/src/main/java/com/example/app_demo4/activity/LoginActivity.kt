package com.example.app_demo4.activity

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.util.Patterns
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.app_demo4.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_home.*

class LoginActivity : AppCompatActivity() {

    // View Properties
    private lateinit var image: ImageView
    private lateinit var header: TextView
    private lateinit var title: TextView
    private lateinit var email: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button

    // Firebase Properties
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        //set firebase property
        mAuth = FirebaseAuth.getInstance()

        /** Look that user is logging-in or not ? -> ดูผู้ใช้งานว่ากำลังล็อกอินอยู่หรือไม่ ? */
        mAuthStateListener = FirebaseAuth.AuthStateListener {
            val user = mAuth.currentUser
            if (user != null) {
                Toast.makeText(this, "Already login", Toast.LENGTH_SHORT).show()
//                Snackbar.make(root_layout_login,"Already login", Snackbar.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
//                Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show()
                Snackbar.make(root_layout_login,"Please login", Snackbar.LENGTH_SHORT).show()
            }
        }

        //set view properties
        image = iv_login_logo
        header = tv_log_in_header
        title = tv_log_in_title
        email = et_login_email
        password = et_login_password
        signInButton = btn_sign_in
        signUpButton = btn_log_in_sign_up

        // Send to sign up with animation
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)

            // Shared Animations (change page --> sign up)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(image, "logo_image"),
                Pair.create(header, "logo_text"),
                Pair.create(title, "logo_title"),
                Pair.create(email, "email_log_in"),
                Pair.create(password, "password_log_in"),
                Pair.create(signInButton, "log_in_sign_in_btn"),
                Pair.create(signUpButton, "log_in_sign_up_btn")
            )
            startActivity(intent, options.toBundle())
        }


        // Validate login & Send to Home
        signInButton.setOnClickListener {

            val valueOfEmail = email.editText?.text.toString().trim()
            val valueOfPassword = password.editText?.text.toString().trim()

            /** -> โยน email & password ไป validate */
            if (!validateEmail(valueOfEmail) || !validatePassword(valueOfPassword)) {
                return@setOnClickListener
            }
            else {
                /** -> เมื่อ validate ถูกต้อง, โยนให้ฟังก์ชั่นล็อกอินด้วย email & password  */
                isUser(valueOfEmail, valueOfPassword)
            }
        }
    }


    /** # Functions here */
    // Check email & password before send to home page
    private fun isUser(valueOfEmail: String, valueOfPassword: String) {

        mAuth.signInWithEmailAndPassword(valueOfEmail, valueOfPassword)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
//                Snackbar.make(root_layout_login,"Login successful", Snackbar.LENGTH_SHORT).show()

                    // send to Home page with uid*
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("userId", mAuth.currentUser!!.uid)
                    startActivity(intent)
                    finish()

                }
                else {
                    email.error = "Wrong Email"
                    password.error = "Wrong Password"
                    email.requestFocus()
//                    password.requestFocus()
                }
            }

        // v.2 (test)
//        val ref = FirebaseDatabase.getInstance().getReference("Users")
//            ref.orderByChild("email").equalTo(valueOfEmail).apply {
//
//                addListenerForSingleValueEvent(object : ValueEventListener {
//
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                        //dataSnapshot = valueOfEmail(email) -เช็คว่ามี email ที่ใส่เข้ามาหรือไม่ ?
//                        if (dataSnapshot.exists()) {
//
//                            // -> ใช่, ให้ sign in ด้วย email & password
//                            mAuth.signInWithEmailAndPassword(valueOfEmail, valueOfPassword)
//                                .addOnCompleteListener {
//
//                                    if (it.isSuccessful) {
//                                        Toast.makeText(
//                                            this@LoginActivity,
//                                            "Login successful",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//                                        // send to Home page with uid*
//                                        val intent =
//                                            Intent(this@LoginActivity, HomeActivity::class.java)
//                                        intent.putExtra("userId", mAuth.currentUser!!.uid)
//                                        startActivity(intent)
//                                        finish()
//
//                                    } else {
//                                        password.error = "Wrong Password"
//                                        password.requestFocus()
//                                    }
//                                }
//                        } else {
//                            // -> หาอีเมลไม่เจอ
//                            email.error = "No such Email Address"
//                            email.requestFocus()
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
////                        finish()
//                        Toast.makeText(this@LoginActivity, "onCancelled", Toast.LENGTH_SHORT).show()
//                    }
//
//                })
//            }

    }


    /** # Login Validations */
    private fun validateEmail(valueOfEmail: String): Boolean {
//        val valueOfEmail: String = req_email.editText?.text.toString()
//        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return when {
            valueOfEmail.isEmpty() -> {
                et_login_email.error = "Field cannot be empty"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(valueOfEmail).matches() -> {
                et_login_email.error = "Invalid email address"
                false
            }
            else -> {
                et_login_email.error = null
                true
            }
        }
    }

    private fun validatePassword(valueOfPassword: String): Boolean {
//        val valueOfPassword: String = req_password.editText?.text.toString()

        return if (valueOfPassword.isEmpty()) {
            et_login_password.error = "Field cannot be empty"
            false
        } else {
            et_login_password.error = null
            true
        }
    }

    /** Looked at */
    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mAuthStateListener)
    }

}