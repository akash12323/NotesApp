package com.example.todofirebase

data class reminders(
    var title:String,
    var details:String,
    var date:Long,
    var time:Long,
    var id:String
){
    constructor():this("","",0L,0L,"")
}