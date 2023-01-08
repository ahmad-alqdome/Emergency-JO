package com.example.car

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterRequest(val context: Context?,private  val resource:Int,private  val data:ArrayList<RequestData>):BaseAdapter(){

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
            val description=requestView!!.findViewById<TextView>(R.id.tv_description_id)
            val name=requestView.findViewById<TextView>(R.id.tv_name_notification_id)
            val nationalNumber=requestView.findViewById<TextView>(R.id.tv_national_number_notification_id)
            val governorate=requestView.findViewById<TextView>(R.id.tv_governorate_notification_id)
            val phoneNumber=requestView.findViewById<TextView>(R.id.tv_phone_number_notification_id)
            val time=requestView.findViewById<TextView>(R.id.tv_time_request_id)

                 description.text=data[position].description
                 name.text=data[position].name
                 nationalNumber.text=data[position].personalID
                 governorate.text=data[position].governorate
                 time.text=data[position].time
                 phoneNumber.text=data[position].phone




        return requestView
    }
}