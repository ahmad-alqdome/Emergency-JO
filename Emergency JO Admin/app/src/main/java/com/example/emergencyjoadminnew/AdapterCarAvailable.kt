package com.example.emergencyjoadminnew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterCarAvailable (private val context: Context, private val resource:Int, var data:ArrayList<DatabaseCar>):BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var availableCar=convertView

        if(availableCar==null)
        {
            availableCar= LayoutInflater.from(context).inflate(resource,parent,false)
        }


        val carNumber=availableCar!!.findViewById<TextView>(R.id.tv_car_number_av_id)
        val carName=availableCar.findViewById<TextView>(R.id.tv_car_name_av_id)
        val carType=availableCar.findViewById<TextView>(R.id.tv_car_type_av_id)
        val militaryName=availableCar.findViewById<TextView>(R.id.tv_military_name_av_id)

        carNumber.text=data[position].carNumber
        carName.text=data[position].carName
        carType.text=data[position].typeCar
        militaryName.text=data[position].militaryName

        return availableCar

    }
}