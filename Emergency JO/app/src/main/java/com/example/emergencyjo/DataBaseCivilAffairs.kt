package com.example.emergencyjo

class DataBaseCivilAffairs {

  var  id:String?=null
    var personalID:String?=null
    var check:String?=null
    var name:String?=null
    var mothername:String?=null
    var gender:String?=null
    var governorate:String?=null
    var birthday:String?=null

    constructor(){}

    constructor( id:String,personalID:String,check:String,name:String,mothername:String,gender:String,governorate:String,birthday:String)
    {
        this.id=id
        this.personalID=personalID
        this.check=check
        this.name=name
        this.mothername=mothername
        this.gender=gender
        this.governorate=governorate
        this.birthday=birthday

    }
}