package com.example.emergencyjo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.header_side_list.view.*


class Main : BaseActivity(), NavigationView.OnNavigationItemSelectedListener  {


//Definition  userName as String
private lateinit var userName:String

//Definition  toolbar as Toolbar
private lateinit var toolbar:Toolbar

//Reference Database
private lateinit var mRefData:DatabaseReference

//Definition  headerView as View
private lateinit var headerView:View

//Store Status values in statusData Array
private lateinit var statusData: ArrayList<Status>

//Reference Database
private lateinit var mRefStatus: DatabaseReference

    var name:String?=null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLon:Double=0.0
    private var currentLat:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Gone To Make elements  invisible
        rg_option.visibility = View.GONE
        et_description_box_id.visibility = View.GONE
        btn_remove_text_id.visibility =View.GONE

        //Definition StatusData As ArrayList
        statusData=ArrayList()

        // access to location
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        //Function to check if user connected to internet if not go to DirectCall Activity
        if ( ! isNetworkConnected() ) {

            val goToDirectCall = Intent(this, DirectCall::class.java)
            startActivity(goToDirectCall)


        }



        toolbar = findViewById(R.id.header_id)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        //Connect Bar Function
        connectActionbar()

        //Connect DataBase Function
        connectDataBase()

        //Store User Name in GetName (Using Get Name Function)
        userName=getName()
        //Toast.makeText(applicationContext, "$userID", Toast.LENGTH_SHORT).show()

        headerActionBar()

        //Get Status Values
        mRefStatus.addValueEventListener(object:ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {

                statusData.clear()
                for(d in snapshot.children)
                {

                    //Get Status Values and store it in StatusData
                    statusData.add(d.getValue(Status::class.java)!!)

                //Toast.makeText(baseContext, "a", Toast.LENGTH_SHORT).show()

                }

                //Store Status Data in Adapter that contain a button
                val adapter=AdapterStatus(applicationContext,R.layout.status_view,statusData)
                gd_status_id.adapter=adapter

            }

            override fun onCancelled(error: DatabaseError) {}

        })



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
        val location=fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it!=null)
            {
                currentLat=it.latitude
                currentLon=it.longitude
            }
        }
    }

    override fun onStart() {
        super.onStart()

         //Click on Status button
        gd_status_id.onItemClickListener= AdapterView.OnItemClickListener { _, _, position, _->

            //Make Radio Group Visible
            rg_option.visibility = View.VISIBLE

            //Set Radio Group Text From Database
            if(statusData[position].option1_name!!.isNotEmpty())
            {
                rb_option_1.text = statusData[position].option1_name
                rb_option_1.visibility=View.VISIBLE
                

            }
            else
                rb_option_1.visibility=View.GONE
            if(statusData[position].option1_name!!.isNotEmpty())
            {
                rb_option_2.text = statusData[position].option2_name
                rb_option_2.visibility=View.VISIBLE

            }
            else
                rb_option_2.visibility=View.GONE
            if(statusData[position].option3_name!!.isNotEmpty())
            {
                rb_option_3.text = statusData[position].option3_name
                rb_option_3.visibility=View.VISIBLE

            }
            else
                rb_option_3.visibility=View.GONE


            //Make description and remove btn INVISIBLE
            et_description_box_id.visibility = View.GONE
            btn_remove_text_id.visibility = View.GONE


           //Set Radio button value in Description Box
            rb_option_1.setOnClickListener{
                et_description_box_id.setText(rb_option_1.text.toString())
            }
            rb_option_2.setOnClickListener{
                et_description_box_id.setText(rb_option_2.text.toString())

            }
            rb_option_3.setOnClickListener{
                et_description_box_id.setText(rb_option_3.text.toString())
            }
        }

        //Click on other button
        btn_other_status.setOnClickListener{

            //Make Description Empty
            et_description_box_id.setText("")

            //Make Radio Group INVISIBLE
            rg_option.visibility = View.GONE

            //Make Description and remove btn Visible
            et_description_box_id.visibility = View.VISIBLE
            btn_remove_text_id.visibility =View.VISIBLE
        }

        //Click on Remove btn to remove description Value
        btn_remove_text_id.setOnClickListener {
            et_description_box_id.setText("")
        }


        //Click on Map Button
        btn_map_id.setOnClickListener()
        {
            //If description box is  not empty will  go to map activity
            if(et_description_box_id.text.toString().isNotEmpty()){

                //Go to Maps Activity
                intent=Intent(this,MapActivity::class.java)

                // Send Description value to database
                intent.putExtra("description",et_description_box_id.text.toString())
                if((ch_ambulance_id.isChecked&&ch_fire_fighter_id.isChecked)||(!ch_ambulance_id.isChecked&&!ch_fire_fighter_id.isChecked))
                    intent.putExtra("type_car","both")
                else if(ch_ambulance_id.isChecked)
                    intent.putExtra("type_car","Ambulance")
                else
                    intent.putExtra("type_car","FireTruck")


                startActivity(intent)
            }

            //If description box is empty will not go to map activity
            else
           {
               et_description_box_id.error="Set Description "
               Toast.makeText(this, "Please Set Description", Toast.LENGTH_SHORT).show()
           }

        }



        //Click on button to go to Dail in phone and set 911
        btn_direct_call_id.setOnClickListener()
        {
            val goToCall =Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
            startActivity(goToCall)
        }


    }

    //Function to view ActionBar (side list + user name)
    private fun  headerActionBar()
    {
        headerView =nav_side_list_id.getHeaderView(0)
        headerView.name_user_side_list_id.text=userName

    }

    //Function to get name from database and store it in side list
    @JvmName("getName1")
    private fun getName(): String
    {
        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        return sharedPreferences.getString(UserProperties.USER_NAME,"").toString()
    }

    //Function to Open and Close the bar
    private fun connectActionbar()
    {
        val actionToggle=ActionBarDrawerToggle(this,drawer_main_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        drawer_main_id.addDrawerListener(actionToggle)
        actionToggle.syncState()
        nav_side_list_id.setNavigationItemSelectedListener(this)
    }

    //All Side Bar items
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            //Side Bar to Logout and go to login
            R.id.logout_side_list_id -> {
                editIdToSharedPreferences()
                val goToLogin = Intent(this, Login::class.java)
                startActivity(goToLogin)
                finish()
            }

            //Side Bar to go to Safety instruction
            R.id.safety_Instructions_side_list_id -> {
                val goToSafety = Intent(this, ChooseSafety::class.java)
                startActivity(goToSafety)
                //finish()
            }

            //Side Bar to go to Setting
            R.id.setting_side_list_id -> {
                val goToSetting = Intent(this, UserSetting::class.java)
                startActivity(goToSetting)
               // finish()
            }

            //Side Bar to go to Home
            R.id.home_side_list_id -> {
                val goToMain = Intent(this, Main::class.java)
                startActivity(goToMain)
                finish()
            }
        }


        closeDrawer()
        return true
    }

    //Fun to close drawer when press back
    override fun onBackPressed() {
        if(drawer_main_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    //Fun to close drawer
    private fun closeDrawer()
    {
        drawer_main_id.closeDrawer(GravityCompat.START)

    }

    //fun to store user ID
    private fun editIdToSharedPreferences() {

        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putString(UserProperties.USER_ID,"0")



        editor.apply()
    }

    //Function to connect to firebase
private fun connectDataBase()
{
    val database=Firebase.database
    //Connect to Emergency user database to get name
    mRefData=database.getReference("Emergency_user")

    //Connect to Common Cases to get status
    mRefStatus=database.getReference("Common_Accident")


}


    private fun isNetworkConnected(): Boolean {
        val cm : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return (cm.activeNetworkInfo != null) && (cm.activeNetworkInfo!!.isConnected)
    }


}
