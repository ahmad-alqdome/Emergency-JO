package com.example.emergencyjo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterStatus(var context:Context, private var resource:Int, var data: ArrayList<Status>):BaseAdapter() {
    override fun getCount(): Int {
        //Get size of data
        return data.size
    }

    override fun getItem(position: Int): Any {
        //check data position
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()   }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view=convertView
        if(view==null)
        {
            view=LayoutInflater.from(context).inflate(resource,parent,false)
        }

        //Definition text as status id from status id xml
        val text=view?.findViewById<TextView>(R.id.tv_status_id)

        //Store case name in text
        text?.text=data[position].accident_name


        return view

    }
}