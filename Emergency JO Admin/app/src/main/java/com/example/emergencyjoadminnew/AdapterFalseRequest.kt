package com.example.emergencyjoadminnew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterFalseRequest(val context: Context?, private val resource:Int, private val data: ArrayList<RequestFalse>):BaseAdapter(){

    override fun getCount(): Int {
return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val requestView=LayoutInflater.from(context).inflate(resource,parent,false)

        val report=requestView.findViewById<TextView>(R.id.tv_false_report_id)
        val description=requestView.findViewById<TextView>(R.id.tv_false_description_id)
        val name=requestView.findViewById<TextView>(R.id.tv_false_name_id)
        val personalID=requestView.findViewById<TextView>(R.id.tv_false_personalID_id)
        val governorate=requestView.findViewById<TextView>(R.id.tv_false_governorate_id)
        val phoneNumber=requestView.findViewById<TextView>(R.id.tv_false_phone_number_id)
        val time=requestView.findViewById<TextView>(R.id.tv_time_false_request_id)
        val militaryName=requestView.findViewById<TextView>(R.id.tv_false_military_name_id)
        val militaryNumber=requestView.findViewById<TextView>(R.id.tv_false_military_number_id)
        val carNumber=requestView.findViewById<TextView>(R.id.tv_false_car_number_id)
        val carName=requestView.findViewById<TextView>(R.id.tv_false_car_name_id)


        report.text=data[position].report
        description.text=data[position].description
        name.text=data[position].name
        personalID.text=data[position].personalID
        governorate.text=data[position].governorate
        time.text=data[position].time
        phoneNumber.text=data[position].phone
        militaryName.text=data[position].military_name
        militaryNumber.text=data[position].military_number
        carName.text=data[position].car_name
        carNumber.text=data[position].car_number

        return requestView
    }
}