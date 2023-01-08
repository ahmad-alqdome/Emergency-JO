package com.example.car

class Exp {

    companion object
    {
        val expCarNumber=Regex("\\d{2}-\\d+")
        val expCarName=Regex("([a-zA-z]+\\s*[a-zA-z]*)|([ا-ي]+\\s*[ا-ي]*)")
        val expCarModel=Regex("\\d{4}")
        val expMilitaryName=Regex("(([a-zA-z]+)\\s+([a-zA-z]+)\\s+([a-zA-z]+)\\s+([a-zA-z]+))|([ا-ي]+\\s+[ا-ي]+\\s+[ا-ي]+\\s+[ا-ي]+)")
        val expMilitaryNumber=Regex("\\d{4,10}")
        val expPassword=Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")


    }
}