package com.example.emergencyjoadminnew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterRemove(var context: Context,var resource:Int,var data:ArrayList<DatabaseCar>):BaseAdapter() {
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




            val removeView=LayoutInflater.from(context).inflate(resource,parent,false)



        val carNumber=removeView?.findViewById<TextView>(R.id.tv_car_number_view_id)
        val carName=removeView?.findViewById<TextView>(R.id.tv_car_name_view_id)
        val carType=removeView?.findViewById<TextView>(R.id.tv_car_type_view_id)
        val militaryName=removeView?.findViewById<TextView>(R.id.tv_military_name_view_id)

        carNumber?.text=data[position].carNumber
        carName?.text=data[position].carName
        carType?.text=data[position].typeCar
        militaryName?.text=data[position].militaryName

        return removeView

    }
}