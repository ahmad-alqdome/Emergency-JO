package com.example.emergencyjoadminnew

data class RequestAccepted (


    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var personalID:String="",
    var name:String="",
    var phone:String="",
    var governorate:String="",
    var description:String="",
    var time:String="",
    var type_car:String=""
)