package com.example.app_demo4.activity

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.app_demo4.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Handler as Handler

class MainActivity : AppCompatActivity() {

    lateinit var handler: Handler

    // Variables
    private var topAnim: Animation? = null
    private var bottomAnim: Animation? = null
    private var image: ImageView? = null
    private var header: TextView? = null
    private var present: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        // [START fcm_runtime_enable_auto_init]
        Firebase.messaging.isAutoInitEnabled = true


        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        // Hooks
        image = iv_main
        header = tv_main
        present = tv_main_present

        image!!.startAnimation(topAnim)
        header!!.startAnimation(bottomAnim)
        present!!.startAnimation(bottomAnim)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, LoginActivity::class.java)

            // Shared Animations (change page --> log in)
            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(image, "logo_image"),
                Pair.create(header, "logo_text")
            )
            startActivity(intent, options.toBundle())
            finish()
        }, 3000) //delay
    }

}

