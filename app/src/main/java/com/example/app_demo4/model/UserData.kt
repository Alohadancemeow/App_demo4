package com.example.app_demo4.model

import com.example.app_demo4.R
import com.squareup.picasso.Picasso

class UserData() {
//    var image: String = "Default url"
    var displayName: String = "Default name"
    var fullName: String = "Default fullName"

    constructor(display_name:String, full_name:String) : this(){
        this.displayName = display_name
        this.fullName = full_name
//        this.image = image
    }

}