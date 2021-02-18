package com.example.app_demo4.model

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.app_demo4.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.progress_btn_layout.view.*

class ProgressButton(view: View) {

    private var cardView: CardView = view.card_view_progress
    private var constraints: ConstraintLayout = view.constraint_layout_progress
    private var progressBar: ProgressBar = view.progressBar
    private var textView: MaterialButton = view.textView_progress

    @SuppressLint("SetTextI18n")
    fun buttonActivated( reqCode: Int) {

        progressBar.visibility = View.VISIBLE

        when (reqCode) {

            //sign in
            1 -> {
                textView.text = "Please wait..."
                textView.setIconResource(R.drawable.ic_sign_in)
            }
            //sign up
            2 -> {
                textView.text = "Please wait..."
                textView.setIconResource(R.drawable.ic_sign_up_white)
            }
            //profile setting
            3 -> {
                textView.text = "Updating..."
                textView.setIconResource(R.drawable.ic_verified_white)
            }
            //your name
            4 -> {
                textView.text = "Saving..."
                textView.setIconResource(R.drawable.ic_check_white)
            }
            //feedback
            5 -> {
                textView.text = "Sending..."
                textView.setIconResource(R.drawable.ic_send_white)
            }
            //general event create
            6 -> {
                textView.text = "Creating..."
                textView.setIconResource(R.drawable.ic_event1_white)
            }
            //special event create
            7 -> {
                textView.text = "Creating..."
                textView.setIconResource(R.drawable.ic_event_white)
            }
            //join
            8 -> {
                textView.text = "Joining..."
                textView.setIconResource(R.drawable.ic_check_white)
            }
            //delete
            9 -> {
                textView.text = "Deleting..."
                textView.setIconResource(R.drawable.ic_delete)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun buttonFinished( reqCode: Int) {

        progressBar.visibility = View.GONE

        when (reqCode) {

            //sign in
            1 -> {
                textView.text = "Login"
                textView.setIconResource(R.drawable.ic_sign_in)
            }
            //sign up
            2 -> {
                textView.text = "Sign up"
                textView.setIconResource(R.drawable.ic_sign_up_white)
            }
            //profile setting
            3 -> {
                textView.text = "Update"
                textView.setIconResource(R.drawable.ic_verified_white)
            }
            //your name
            4 -> {
                textView.text = "Save"
                textView.setIconResource(R.drawable.ic_check_white)
            }
            //feedback
            5 -> {
                textView.text = "Send feedback"
                textView.setIconResource(R.drawable.ic_send_white)
            }
            //general event create
            6 -> {
                textView.text = "Create"
                textView.setIconResource(R.drawable.ic_event1_white)
            }
            //special event create
            7 -> {
                textView.text = "Create"
                textView.setIconResource(R.drawable.ic_event_white)
            }
            //join
            8 -> {
                textView.text = "Join"
                textView.setIconResource(R.drawable.ic_check_white)
            }
            //delete
            9 -> {
                textView.text = "Delete"
                textView.setIconResource(R.drawable.ic_delete)
            }

        }
    }


}