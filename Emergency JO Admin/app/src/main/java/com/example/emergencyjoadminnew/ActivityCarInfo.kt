package com.example.emergencyjoadminnew

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
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
import kotlinx.android.synthetic.main.activity_car_info.*
import kotlinx.android.synthetic.main.activity_car_info.nav_side_list_id

class ActivityCarInfo : BaseActivity() ,NavigationView.OnNavigationItemSelectedListener {

    // global variables
    private lateinit var mRefCar: DatabaseReference
    lateinit var dataCar: ArrayList<DatabaseCar>
    lateinit var numberCar:ArrayList<String>
    lateinit var adapterCarInfo: ArrayAdapter<String>
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_info)

        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()

        dataCar= ArrayList()
        numberCar= ArrayList()
        connectDatabase()
        mRefCar.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                dataCar.clear()
                for (data in snapshot.children) {
                    dataCar.add(data.getValue(DatabaseCar::class.java)!!)
                }
                numberCar.clear()
                for (index in 0 until dataCar.size) {
                    numberCar.add(dataCar[index].carNumber)
                }

                adapterCarInfo =
                    ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, numberCar)
                adapterCarInfo.notifyDataSetChanged()
                ls_car_info_id.adapter = adapterCarInfo


        }
            override fun onCancelled(error: DatabaseError) {}

        })

        search_car_info_id.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                adapterCarInfo.filter.filter(newText)

                return false
            }

        })// end search view


        ls_car_info_id.onItemClickListener=
            AdapterView.OnItemClickListener{ _, _, position, _->

            savedIdToSharedPreferences(dataCar[position])

                val goToShowCarInfo=Intent(this,ActivityShowCarInfo::class.java)
                startActivity(goToShowCarInfo)

            }
    }
    private fun connectDatabase()
    {
        val database= Firebase.database
        mRefCar=database.getReference("Car")
    }
    private fun savedIdToSharedPreferences(data:DatabaseCar) {

        val sharedPreferences=getSharedPreferences(CarProperties.FILE_NAME, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()

        editor.putString(CarProperties.CAR_NUMBER,data.carNumber)
        editor.putString(CarProperties.CAR_NAME,data.carName)
        editor.putString(CarProperties.CAR_MODEL,data.carModel)
        editor.putString(CarProperties.CAR_TYPE,data.typeCar)
        editor.putString(CarProperties.MILITARY_NAME,data.militaryName)
        editor.putString(CarProperties.MILITARY_NUMBER,data.militaryNumber)
        editor.putString(CarProperties.PASSWORD,data.password)
        editor.putString(CarProperties.USERNAME,data.username)

        editor.apply()
    }

    /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_car_info_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_car_info_id.addDrawerListener(actionToggle)
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
        dr_car_info_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_car_info_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    /****************************************************************************************/




}