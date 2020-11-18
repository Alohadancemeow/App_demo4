package com.example.app_demo4.model

class UserData() {
//    var image: String = "Default url"
    var display_name: String? = null
    var full_name: String? = null

    constructor(display_name:String, full_name:String) : this(){
        this.display_name = display_name
        this.full_name = full_name
//        this.image = image
    }

}