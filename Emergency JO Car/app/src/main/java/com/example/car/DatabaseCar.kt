package com.example.car


class DatabaseCar {



    var carNumber:String?=null
    var carName:String?=null
    var carModel:String?=null
    var militaryName:String?=null
    var militaryNumber:String?=null
    var username:String?=null
    var password:String?=null
    var typeCar:String?=null
    var status:String?=null
    var request:RequestData?=null

    constructor()
    {
    }

    constructor(

        number:String,
        name:String,
        model:String,
        militaryName:String,
        militaryNumber:String,
        username:String,
        password: String,
        typeCar: String,
        status:String
        ,request: RequestData?
    )
    {

        this.carNumber=number
        this.carName=name
        this.carModel=model
        this.militaryName=militaryName
        this.militaryNumber=militaryNumber
        this.username=username
        this.password=password
        this.typeCar=typeCar
        this.status=status
        this.request=request

    }
}