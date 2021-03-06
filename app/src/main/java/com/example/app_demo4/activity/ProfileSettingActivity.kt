package com.example.app_demo4.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.app_demo4.R
import com.example.app_demo4.model.ProgressButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_profile_setting.*
import kotlinx.android.synthetic.main.progress_btn_layout.*

class ProfileSettingActivity : AppCompatActivity() {

    // Firebase Properties
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var userId: String

    // View Properties
    private lateinit var displayName: TextView
    private lateinit var fullName: TextView
    private lateinit var status: TextInputLayout
    private lateinit var temple: TextInputLayout
    private lateinit var kana: TextInputLayout
    private lateinit var age: TextInputLayout
    private lateinit var email: TextInputLayout
    private lateinit var phone: TextInputLayout

    private lateinit var progressBarProfileSetting: View

    //Top Appbar
    private lateinit var topAppBar: MaterialToolbar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(FLAG_FULLSCREEN)
        setContentView(R.layout.activity_profile_setting)

        //set firebase properties
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseFirestore.getInstance()
        userId = mAuth.currentUser!!.uid

        // set Top appbar
        topAppBar = top_AppBar_profile_setting.apply {
            topAppBarAction(this)
        }


        //set view properties
        displayName = tv_display_name_setting
        fullName = tv_full_name_setting
        status = status_setting
        temple = temple_setting
        kana = kana_setting
        age = age_setting
        email = email_setting
        phone = phone_setting

        dropDownOptions()

        getCurrentUser()


        //set text and icon on Button
        val textViewButton: MaterialButton = textView_progress
        textViewButton.apply {
            this.text = "Update"
            this.setIconResource(R.drawable.ic_verified_white)
        }


        // # Update data and send to home
        progressBarProfileSetting = profile_setting_progressbar_button.apply {

            setOnClickListener {

                //call progressbar
                val progressButton = ProgressButton(it)
                progressButton.buttonActivated(3)

                //delay
                val handle = Handler()
                handle.postDelayed({

                    /** -> อัพเดตข้อมูลพร้อมโยน uid ไปด้วย */
                    updateData(userId)

                    //end progressbar
                    progressButton.buttonFinished(3)

                }, 2000)

            }
        }


        // -> Edit name button
        iv_edit_setting.setOnClickListener {
            val intent = Intent(this, YourNameActivity::class.java)
            startActivity(intent)
        }

    }


    /** # Functions here */

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

    private fun dropDownOptions() {
        //dropdown options
        val dropdownTextView = dropdown_text
        val items = listOf("Monk", "Novice")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, items)
        dropdownTextView.setAdapter(adapter)
    }

    private fun getCurrentUser() {
        //get current user
        val userRef = mDatabase.collection("Users").document(userId)
        userRef.addSnapshotListener { value, _ ->

           value.let {

                /** -> ดึงข้อมูลของผู้ใช้ที่กำลัง login */
                val displayNameFromDB = value?.get("display_name").toString()
                val fullNameFromDB = value?.get("full_name").toString()
                val statusFromDB = value?.get("status").toString()
                val templeFromDB = value?.get("wat").toString()
                val kanaFromDB = value?.get("kana").toString()
                val ageFromDB = value?.get("age").toString()
                val emailFromDB = value?.get("email").toString()
                val phoneFromDB = value?.get("phone").toString()

                /** -> ใส่ข้อมูลที่ดึงมาลงในแต่ละช่อง */
                displayName.text = displayNameFromDB
                fullName.text = fullNameFromDB
                status.editText?.setText(statusFromDB)
                temple.editText?.setText(templeFromDB)
                kana.editText?.setText(kanaFromDB)
                age.editText?.setText(ageFromDB)
                email.editText?.setText(emailFromDB)
                phone.editText?.setText(phoneFromDB)
            }
        }
    }


    private fun updateData(userId: String) {

        /** -> ดึงข้อมูลที่จะอัพเดต newData */
        val newStatus = status_setting.editText?.text.toString()
        val newTemple = temple_setting.editText?.text.toString()
        val newKana = kana_setting.editText?.text.toString()
        val newAge = age_setting.editText?.text.toString()
        val newEmail = email_setting.editText?.text.toString()
        val newPhone = phone_setting.editText?.text.toString()

        /** -> ส่ง newData ไป validate พร้อม uid เพื่ออัพเดตขึ้น firebase ต่อ */
        validateData(newStatus, newTemple, newKana, newAge, newEmail, newPhone, userId)

    }


    private fun validateData(
        newStatus: String,
        newTemple: String,
        newKana: String,
        newAge: String,
        newEmail: String,
        newPhone: String,
        userId: String
    ) {

        if (!normalValidate(
                newStatus,
                newTemple,
                newKana,
                newAge
            ) || !validateEmail(newEmail) || !validatePhone(newPhone)
        ) {
            return
        } else {

            /** -> set new data ที่ผ่านการ validate แล้วลงในแต่ละช่อง */
            val newData = HashMap<String, Any>().apply {
                this["status"] = newStatus
                this["wat"] = newTemple
                this["kana"] = newKana
                this["age"] = newAge
                this["email"] = newEmail
                this["phone"] = newPhone
            }

            /** -> อัพเดตข้อมูลขึ้น Firebase database */
            mDatabase.collection("Users").document(userId).update(newData)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()

                        /** -> เมื่ออัพเดตเสร็จแล้ว ส่งไปหน้าหลัก */
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Update Unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    /** # Validations */
    private fun normalValidate(
        newStatus: String,
        newTemple: String,
        newKana: String,
        newAge: String
    ): Boolean {

        return when {
            newStatus.isEmpty() -> {
                status_setting.error = "Field cannot be empty"
                false
            }
            newTemple.isEmpty() -> {
                temple_setting.error = "Field cannot be empty"
                false
            }
            newKana.isEmpty() -> {
                kana_setting.error = "Field cannot be empty"
                false
            }
            newAge.isEmpty() -> {
                age_setting.error = "Field cannot be empty"
                false
            }
            else -> {
                status_setting.error = null
                temple_setting.error = null
                kana_setting.error = null
                age_setting.error = null
                true
            }
        }
    }

    private fun validateEmail(newEmail: String): Boolean {
//        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return when {
            newEmail.isEmpty() -> {
                email_setting.error = "Field cannot be empty"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(newEmail).matches() -> {
                email_setting.error = "Invalid email address"
                false
            }
            else -> {
                email_setting.error = null
                true
            }
        }
    }

    private fun validatePhone(newPhone: String): Boolean {

        return when {
            newPhone.isEmpty() -> {
                phone_setting.error = "Field cannot be empty"
                false
            }
            newPhone.length != 10 -> {
                phone_setting.error = "Invalid phone number"
                false
            }
            else -> {
                phone_setting.error = null
                true
            }
        }
    }

}