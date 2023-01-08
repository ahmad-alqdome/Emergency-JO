package com.example.emergencyjoadminnew

import android.Manifest
import android.app.AlertDialog
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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zeugmasolutions.localehelper.Locales
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.nav_side_list_id
import kotlin.collections.ArrayList

class ActivityMain : BaseActivity() , NavigationView.OnNavigationItemSelectedListener {

    // global variables
    private lateinit var toolbar:Toolbar
    private lateinit var requestData : ArrayList<RequestData>
    private lateinit var requestItem:RequestData
    private lateinit var mRefRequests: DatabaseReference
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLon:Double=0.0
    private var currentLat:Double=0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        connDatabase()
        requestData= ArrayList()
        registerForContextMenu(grid_request_id)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        getLocation()

        mRefRequests.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestData.clear()
                for(d in snapshot.children)
                {
                    requestData.add(d.getValue(RequestData::class.java)!!)
                }
                val adapter = AdapterRequest(baseContext, R.layout.view_request, requestData)
                adapter.notifyDataSetChanged()
                grid_request_id.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}

        })



    }

    override fun onStart() {
        super.onStart()

        grid_request_id.onItemLongClickListener= AdapterView.OnItemLongClickListener { _, _, position, _->

            requestItem=requestData[position]

            false
        }
        grid_request_id.onItemClickListener=AdapterView.OnItemClickListener{
            _,_,position,_ ->

            savedIdToSharedPreferences(requestData[position])
            val goToAvailableCar=Intent(this,ActivityAvailableCar::class.java)
            startActivity(goToAvailableCar)

        }





        } // end on start


    private fun getLocation() {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            return
        }
        val location=fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it!=null)
            {
                currentLat=it.latitude
                currentLon=it.longitude
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?)
    {

        val menuInflater= MenuInflater(this)
        menuInflater.inflate(R.menu.request_list,menu)
    }

    // on select delete in context menu will remove data in fire base
    override fun onContextItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.send_request_id->
            {

                savedIdToSharedPreferences(requestItem)
                val goToAvailableCar=Intent(this,ActivityAvailableCar::class.java)
                startActivity(goToAvailableCar)


            }
            R.id.show_location_id->
            {
                val lon=requestItem.lon
                val lat=requestItem.lat
                val gmmIntentUri= Uri.parse("https://www.google.co.in/maps/dir/$currentLat,$currentLon/$lat,$lon")
                val mapIntent=Intent(Intent.ACTION_VIEW,gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                startActivity(mapIntent)
            }
            R.id.remove_request_id->{
                showAlertSure(requestItem.personalID)
            }

        }
        return true
    }




    private fun connDatabase()
    {
        val database= Firebase.database
        mRefRequests=database.getReference("Requests")

    }


  /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_main_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_main_id.addDrawerListener(actionToggle)
        actionToggle.syncState()
        nav_side_list_id.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

    when(item.itemId)
    {

        R.id.btn_request_id->
        {
            val goToRequest=Intent(this,ActivityMain::class.java)
            startActivity(goToRequest)
            finish()
        }
        R.id.btn_add_car_id->
        {
            val goToAddCar=Intent(this,ActivityAddCar::class.java)
            startActivity(goToAddCar)
            finish()
        }
        R.id.btn_edit_car_id->
        {
            val goToEditCar=Intent(this,ActivityEditCar::class.java)
            startActivity(goToEditCar)
            finish()
        }
        R.id.btn_remove_car_id->
        {
            val goToRemoveCar=Intent(this,ActivityRemoveCar::class.java)
            startActivity(goToRemoveCar)
            finish()
        }

        R.id.btn_car_info_id->
        {
            val goToCarInfo=Intent(this,ActivityCarInfo::class.java)
            startActivity(goToCarInfo)
            finish()
        }
        R.id.btn_common_cases_id->
        {
            val goToCommonCase=Intent(this,ActivityCommonCase::class.java)
            startActivity(goToCommonCase)
            finish()
        }
        R.id.btn_accepted_request_id->
        {
            val goToAcceptedRequest=Intent(this,ActivityAcceptedRequest::class.java)
            startActivity(goToAcceptedRequest)
            finish()
        }
        R.id.btn_false_request_id->
        {
            val goToFalseRequest=Intent(this,ActivityFalseRequest::class.java)
            startActivity(goToFalseRequest)
            finish()
        }
        R.id.en_id->
        {
            updateLocale(Locales.English)
        }
        R.id.ar_id->
        {
            updateLocale(Locales.Arabic)
        }
        R.id.btn_logout_id->{

            val user: FirebaseAuth = FirebaseAuth.getInstance()

            user.signOut()
            startActivity(
                Intent(this,ActivityLogin::class.java)
            )
            finish()
        }

    }
        closeDrawer()
        return true
    }

    private fun closeDrawer()
    {
        dr_main_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_main_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

/****************************************************************************************/

    private fun showAlertSure(item:String)
    {
        val alertBuilder= AlertDialog.Builder(this)
        alertBuilder.setMessage(getString(R.string.message_remove_request))
        alertBuilder.setPositiveButton(getString(R.string.yes), null)
        alertBuilder.setNegativeButton(getString(R.string.no),null)

        val alertDialog=alertBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            mRefRequests.child(item).removeValue()
            alertDialog.dismiss()

        }
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alertDialog.dismiss()
        }
    }

    private fun savedIdToSharedPreferences(data:RequestData) {

        val sharedPreferences: SharedPreferences =getSharedPreferences(RequestProperties.FILE_NAME, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()


        editor.putString(RequestProperties.LON,data.lon.toString())
        editor.putString(RequestProperties.LAT,data.lat.toString())
        editor.putString(RequestProperties.PERSONAL_ID,data.personalID)
        editor.putString(RequestProperties.NAME,data.name)
        editor.putString(RequestProperties.PHONE,data.phone)
        editor.putString(RequestProperties.GOVERNORATE,data.governorate)
        editor.putString(RequestProperties.DESCRIPTION,data.description)
        editor.putString(RequestProperties.TIME,data.time)


        editor.apply()
    }
} //end class