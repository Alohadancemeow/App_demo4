package com.example.app_demo4.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.app_demo4.R
import com.example.app_demo4.fragment.EventFragment
import com.example.app_demo4.fragment.HomeFragment
import com.example.app_demo4.fragment.UsersFragment
import com.example.app_demo4.notification.AlarmService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismaeldivita.chipnavigation.ChipNavigationBar.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.time.ExperimentalTime


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var alarmService: AlarmService

    // Firebase Properties
    private lateinit var  mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userId: String


    // Nav-Drawer Properties
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private lateinit var topAppBar: MaterialToolbar

    // bottom menu Properties
//    private var bottomMenuLayout: RelativeLayout? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController


    // FAB Properties
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private var clicked = false


    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(FLAG_FULLSCREEN)
        setContentView(R.layout.activity_home)

        alarmService = AlarmService(this)


        //set firebase properties
        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        userId = mAuth.currentUser!!.uid


        // Menu Hooks
        drawerLayout = drawer_layout
        navigationView = navigation_view
        topAppBar = top_AppBar

        // bottom menu
        bottomNavigationView = bottom_nav_menu_home
        navController = findNavController(R.id.fragment)

//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.usersFragment, R.id.eventFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)


        bottomNavigationView.setupWithNavController(navController)




//        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment())
//            .commit()

        navigationDrawer()
        floatingActionBar()

    }


    /** Functions here */

    // FAB Functions
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    @SuppressLint("RestrictedApi")
    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            fab2.visibility = View.VISIBLE
            fab3.visibility = View.VISIBLE
            tv_event_a.visibility = View.VISIBLE
            tv_event_b.visibility = View.VISIBLE
        } else {
            fab2.visibility = View.INVISIBLE
            fab3.visibility = View.INVISIBLE
            tv_event_a.visibility = View.INVISIBLE
            tv_event_b.visibility = View.INVISIBLE
        }

    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            fab2.startAnimation(fromBottom)
            fab3.startAnimation(fromBottom)
            tv_event_a.startAnimation(fromBottom)
            tv_event_b.startAnimation(fromBottom)
            fab1.startAnimation(rotateOpen)
        } else {
            fab2.startAnimation(toBottom)
            fab3.startAnimation(toBottom)
            tv_event_a.startAnimation(toBottom)
            tv_event_b.startAnimation(toBottom)
            fab1.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            fab2.isClickable = true
            fab3.isClickable = true
        } else {
            fab2.isClickable = false
            fab3.isClickable = false
        }
    }


    // Nav-Drawer Functions
    private fun navigationDrawer() {

        // Navigation Drawer
        navigationView?.bringToFront()
        navigationView?.setNavigationItemSelectedListener(this)
        navigationView!!.setCheckedItem(R.id.nav_profile)

        // IconMenu active
        topAppBar.setNavigationOnClickListener {
            if (drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                drawerLayout?.closeDrawer(GravityCompat.START)
            }
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    private fun floatingActionBar() {
        // fab menu
        fab1.setOnClickListener {
            onAddButtonClicked()
        }
        // event 2
        fab2.setOnClickListener {
//            Toast.makeText(this, "Event B", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CreateEvent2Activity::class.java)
            startActivity(intent)
        }
        //event 1
        fab3.setOnClickListener {
//            Toast.makeText(this, "Event A", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CreateEvent1Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        return when (p0.itemId) {

            // Home
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileSettingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_your_name -> {
                val intent = Intent(this, YourNameActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_logout -> {
                mAuth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            // Communication
//            R.id.nav_notify -> {
//                //TODO("help ?")
//                val intent = Intent(this, NotificationDemoActivity::class.java)
//                startActivity(intent)
//                true
//            }

            R.id.nav_feedback -> {
                //TODO("info ?")
                val intent = Intent(this, SendFeedbackActivity::class.java)
                startActivity(intent)
                true
            }

            // error
            else -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                true
            }
        }
    }


}


