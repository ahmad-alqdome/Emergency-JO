package com.example.car

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zeugmasolutions.localehelper.Locales
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.false_dialog.view.*

class MainActivity : BaseActivity() {

    private lateinit var mRefCar: DatabaseReference
    private lateinit var username:String
    private lateinit var myCar:String
    private var STATUS:String="status"
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var status:String=""
    private var REQUEST: String = "request"
    private lateinit var requestData: ArrayList<RequestData>
    private lateinit var requestDataFilter: ArrayList<RequestData>
    private lateinit var requestDataCar: ArrayList<RequestData>


    private var p:Int=-1
    private lateinit var request: RequestData
    private lateinit var mRefAcceptedRequest: DatabaseReference
    private lateinit var mRefRequest: DatabaseReference
    private lateinit var mRefFalseRequest: DatabaseReference


    companion object {
        var currentLon:Double=0.0
        var currentLat:Double=0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectDatabase()
        requestData = ArrayList()
        requestDataFilter= ArrayList()
        requestDataCar= ArrayList()
        registerForContextMenu(grid_show_request)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        username=getData(CarProperties.USERNAME)
        myCar=getData(CarProperties.CAR_TYPE)

        // for set status to button from database
        mRefCar.child(username).child(STATUS).addValueEventListener(object :ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {

                status=snapshot.getValue(String::class.java)!!
                if(status=="Available")
                {
                    //mRefCar.child(username).child(STATUS).setValue("Available")
                    btn_status_id.background = resources.getDrawable(R.drawable.bg_available)
                    btn_status_id.text = getString(R.string.available)
                }
                else
                {
                    btn_status_id.background = resources.getDrawable(R.drawable.bg_unavailable)
                    btn_status_id.text = getString(R.string.un_available)
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })




        // get data request from database
        mRefRequest.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                requestData.clear()
                for (data in snapshot.children) {
                    requestData.add(data.getValue(RequestData::class.java)!!)
                }


                //requestDataFilter.clear()
                for(index in 0 until requestData.size)
                {
                    if((requestData[index].type_car==myCar||requestData[index].type_car=="both")&&status=="Available")
                    {
                        requestDataFilter.add(requestData[index])
                        mRefCar.child(username).child(STATUS).setValue("UnAvailable")

                    }
                }
                val adapterRequest = AdapterRequest(baseContext, R.layout.view_notification_message, requestDataFilter)
                grid_show_request.adapter = adapterRequest
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        mRefCar.child(username).child(REQUEST).addValueEventListener(object :ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestDataCar.clear()
                for (data in snapshot.children) {
                    requestDataCar.add(data.getValue(RequestData::class.java)!!)
                }


                //requestDataFilter.clear()
                for(index in 0 until requestData.size)
                {
                    if((requestDataCar[index].type_car==myCar||requestData[index].type_car=="both")&&status=="Available")
                    {
                        requestDataFilter.add(requestDataCar[index])
                        mRefCar.child(username).child(STATUS).setValue("UnAvailable")

                    }
                }
                val adapterRequest = AdapterRequest(baseContext, R.layout.view_notification_message, requestDataFilter)
                grid_show_request.adapter = adapterRequest
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    } //end onCreate

    override fun onStart() {
        super.onStart()

        // change mode
        btn_status_id.setOnClickListener()
        {
            if(status=="UnAvailable") {

                mRefCar.child(username).child(STATUS).setValue("Available")

            }
            else
            {
                mRefCar.child(username).child(STATUS).setValue("UnAvailable")

            }
        } //end status button

//log out  delete local information and change mode to unavailable
        btn_logout_id.setOnClickListener{
            mRefCar.child(username).child(STATUS).setValue("UnAvailable")
            val goToLogin=Intent(this,Login::class.java)
            editShared()
            startActivity(goToLogin)
            finish()
        }

        btn_request_id.setOnClickListener()
        {
            val refresh=Intent(this,MainActivity::class.java)
            startActivity(refresh)
            finish()
        }



        grid_show_request.onItemClickListener =AdapterView.OnItemClickListener()   // click
        { _, _, position, _ ->
            openLocationAndChangeMode(position)
        }

        grid_show_request.onItemLongClickListener = AdapterView.OnItemLongClickListener() // long Click
        { _, _, position, _ ->
            request = requestDataFilter[position]
            p=position
            false
        }


            ar_id.setOnClickListener {
                updateLocale(Locales.Arabic)
            }
            en_id.setOnClickListener {
                updateLocale(Locales.English)
            }


    }
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.list, menu)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.mn_accept_id -> {
                messageDone()
            }
            R.id.mn_false_request_id -> {
                messageFalseRequest()
            }
            R.id.mn_remove_request_id->{
                removeItem(p)
            }
        }
        return true
    }

    private fun removeItem(position:Int) {
        val alertBuilder = AlertDialog.Builder(this)

        alertBuilder.setMessage(getString(R.string.message_remove_request))
        alertBuilder.setPositiveButton(getString(R.string.yes), null)
        alertBuilder.setNegativeButton(getString(R.string.no), null)
        val alertDialog = alertBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {

            requestDataFilter.removeAt(position)
            alertDialog.dismiss()
            mRefCar.child(username).child(STATUS).setValue("Available")
        }
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alertDialog.dismiss()
        }

    }

    private fun messageFalseRequest(){
        val alertBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.false_dialog, null)
        alertBuilder.setView(view)
        val alertDialog = alertBuilder.create()
        alertDialog.show()
        view.btn_send_alert_id.setOnClickListener()
        {
            if(view.et_report_alert_id.text.isNotEmpty()) {
                val key = mRefFalseRequest.push().key.toString()
                mRefFalseRequest.child(key).setValue(
                    FalseRequest(
                        view.et_report_alert_id.text.toString(),
                        request.lat,
                        request.lat,
                        request.personalID,
                        request.name,
                        request.phone,
                        request.governorate,
                        request.description,
                        request.time,
                        username,
                        getDataShared(CarProperties.CAR_NAME),
                        getDataShared(CarProperties.MILITARY_NAME),
                        getDataShared(CarProperties.MILITARY_NUMBER)
                    )
                )
                alertDialog.dismiss()
                emptyRequest()
            }
            else
            {
                view.et_report_alert_id.error="Can't Empty"
            }
        }
        view.btn_cancel_alert_id.setOnClickListener()
        {
            alertDialog.dismiss()
        }

    }
    private fun messageDone(){

        val alertBuilder=AlertDialog.Builder(this)

        alertBuilder.setMessage(getString(R.string.message_done_request))
        alertBuilder.setPositiveButton(getString(R.string.yes),null)
        alertBuilder.setNegativeButton(getString(R.string.no),null)
        val alertDialog=alertBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {

          emptyRequest()

            val key =mRefAcceptedRequest.push().key.toString()
            mRefAcceptedRequest.child(key).setValue(request)
            mRefCar.child(username).child(REQUEST).child(request.personalID).removeValue()
            alertDialog.dismiss()

        }
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alertDialog.dismiss()
        }

    }

    private fun emptyRequest() {
        requestDataFilter.clear()
        val adapterRequest = AdapterRequest(baseContext, R.layout.view_notification_message, requestDataFilter)
        grid_show_request.adapter = adapterRequest
        mRefCar.child(username).child(STATUS).setValue("Available")

    }

    private fun getDataShared(item: String): String {
        val sharedPreferences = getSharedPreferences(
            CarProperties.FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(item, "").toString()
    }
    private fun openLocationAndChangeMode(position:Int) {
        val alertBuilder = AlertDialog.Builder(this)

        alertBuilder.setMessage(getString(R.string.message_change_mode))
        alertBuilder.setPositiveButton(getString(R.string.yes), null)
        alertBuilder.setNegativeButton(getString(R.string.no), null)
        val alertDialog = alertBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {

            mRefRequest.child(requestDataFilter[position].personalID).removeValue()
            mRefCar.child(username).child(STATUS).setValue("UnAvailable")
            val lon = requestDataFilter[position].lon
            val lat = requestDataFilter[position].lat
            val gmmIntentUri = Uri.parse("https://www.google.co.in/maps/dir/$currentLat,$currentLon/$lat,$lon")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            alertDialog.dismiss()
            startActivity(mapIntent)
        }
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alertDialog.dismiss()
        }

    }
    private fun connectDatabase()
    {
        val database=Firebase.database
        mRefCar=database.getReference("Car")
        mRefAcceptedRequest = database.getReference("Done_Request")
        mRefRequest = database.getReference("Requests")
        mRefFalseRequest = database.getReference("False_Request")


    }// end connect data base
    private fun getData(data:String): String
    {
        val sharedPreferences=getSharedPreferences(CarProperties.FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(data,"").toString()
    } // end getName
    private fun editShared()
    {
        val sharedPreferences: SharedPreferences =getSharedPreferences(CarProperties.FILE_NAME, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putString(CarProperties.USERNAME,"")
        editor.apply()
    }
    private fun getLocation() {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            return
        }
        val location= fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it!=null)
            {
                currentLat=it.latitude
                currentLon=it.longitude
            }
        }
    }
}