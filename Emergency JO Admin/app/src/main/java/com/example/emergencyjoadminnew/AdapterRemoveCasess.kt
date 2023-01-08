package com.example.emergencyjoadminnew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterRemoveCasess(var context: Context,var resource:Int,var data:ArrayList<CommonCases>):BaseAdapter() {
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

        var removeView=convertView

        if(removeView==null)
        {
            removeView=LayoutInflater.from(context).inflate(resource,parent,false)
        }



        val caseName=removeView?.findViewById<TextView>(R.id.tv_case_name)
        val option1_name=removeView?.findViewById<TextView>(R.id.tv_option_1)
        val option2_name=removeView?.findViewById<TextView>(R.id.tv_option_2)
        val option3_name=removeView?.findViewById<TextView>(R.id.tv_option_3)


        caseName?.text=data[position].accident_name
        option1_name?.text=data[position].option1_name
        option2_name?.text=data[position].option2_name
        option3_name?.text=data[position].option3_name



        return removeView

    }
}