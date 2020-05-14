package com.example.todofirebase

data class usernote(
    val title:String,
    val details:String,
    val id:String
){
    constructor():this("","","")
}