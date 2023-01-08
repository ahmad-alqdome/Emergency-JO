package com.example.emergencyjoadminnew

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.zeugmasolutions.localehelper.Locales
import kotlinx.android.synthetic.main.activity_show_car_info.*
import kotlinx.android.synthetic.main.activity_show_car_info.nav_side_list_id

class ActivityShowCarInfo : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_car_info)

        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        
        tv_car_number_show_id.text=getData(CarProperties.CAR_NUMBER)
        tv_car_name_show_id.text=getData(CarProperties.CAR_NAME)
        tv_car_model_show_id.text=getData(CarProperties.CAR_MODEL)
        tv_car_type_show_id.text=getData(CarProperties.CAR_TYPE)
        tv_military_name_show_id.text=getData(CarProperties.MILITARY_NAME)
        tv_military_number_show_id.text=getData(CarProperties.MILITARY_NUMBER)
        tv_username_show_id.text=getData(CarProperties.USERNAME)
        tv_password_show_id.text=getData(CarProperties.PASSWORD)
    }

    private fun getData(item:String): String
    {
        val sharedPreferences=getSharedPreferences(CarProperties.FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(item,"").toString()
    }

    /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_show_car_info_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_show_car_info_id.addDrawerListener(actionToggle)
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
        dr_show_car_info_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_show_car_info_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    /****************************************************************************************/
}