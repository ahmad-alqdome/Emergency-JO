package com.example.emergencyjoadminnew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zeugmasolutions.localehelper.Locales
import kotlinx.android.synthetic.main.activity_available_car.*
import kotlinx.android.synthetic.main.activity_available_car.nav_side_list_id

class ActivityAvailableCar : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    // global  variables
    private lateinit var mRefCar: DatabaseReference
    private lateinit var bigData: ArrayList<DatabaseCar>
    private lateinit var availableCar: ArrayList<DatabaseCar>
    private  val REQUEST:String="request"
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_car)


        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        connectDataBase()
        bigData= ArrayList()
        availableCar= ArrayList()

        mRefCar.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {

                bigData.clear()
                for(data in snapshot.children)
                {
                    bigData.add(data.getValue(DatabaseCar::class.java)!!)
                }

                availableCar.clear()
                for( index in 0 until bigData.size)
                {
                    if(bigData[index].status=="Available")
                        availableCar.add(bigData[index])
                }

                val  adapter=AdapterCarAvailable(baseContext,R.layout.view_available_car,availableCar)
                grid_available_car_id.adapter=adapter
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onStart() {
        super.onStart()

        grid_available_car_id.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            val request= RequestData(getData(RequestProperties.LAT).toDouble(),getData(RequestProperties.LON).toDouble()
                ,getData(RequestProperties.PERSONAL_ID),getData(RequestProperties.NAME),getData(RequestProperties.PHONE)
                ,getData(RequestProperties.GOVERNORATE),getData(RequestProperties.DESCRIPTION),getData(RequestProperties.TIME)
            )

            mRefCar.child(availableCar[position].carNumber).child(REQUEST).child(getData(RequestProperties.PERSONAL_ID)).setValue(request)



        finish()
        }
    }

    private fun getData(key:String): String
    {
        val sharedPreferences=getSharedPreferences(RequestProperties.FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key,"").toString()
    }

    private fun connectDataBase()
    {
        val database= Firebase.database
        mRefCar=database.getReference("Car")
    }

    /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_available_car_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_available_car_id.addDrawerListener(actionToggle)
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
        dr_available_car_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_available_car_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    /****************************************************************************************/

}