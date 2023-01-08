package com.example.emergencyjoadminnew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterAcceptedRequest(val context: Context?, private val resource:Int, private val data: ArrayList<RequestAccepted>):BaseAdapter(){

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
        var requestView: View? =convertView

        if(requestView==null)
        {
            requestView=LayoutInflater.from(context).inflate(resource,parent,false)
        }
            val description=requestView!!.findViewById<TextView>(R.id.tv_accepted_description_id)
            val name=requestView.findViewById<TextView>(R.id.tv_accepted_name_id)
            val nationalNumber=requestView.findViewById<TextView>(R.id.tv_accepted_national_number_id)
            val governorate=requestView.findViewById<TextView>(R.id.tv_accepted_governorate_id)
            val phoneNumber=requestView.findViewById<TextView>(R.id.tv_accepted_phone_number_id)
            val time=requestView.findViewById<TextView>(R.id.tv_time_accepted_request_id)

                 description.text=data[position].description
                 name.text=data[position].name
                 nationalNumber.text=data[position].personalID
                 governorate.text=data[position].governorate
                 time.text=data[position].time
                 phoneNumber.text=data[position].phone




        return requestView
    }
}