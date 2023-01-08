package com.example.emergencyjoadminnew

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_false_request.*
import kotlinx.android.synthetic.main.activity_main.nav_side_list_id

class ActivityFalseRequest : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var mRefFalse: DatabaseReference
    private lateinit var falseData:ArrayList<RequestFalse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_false_request)

        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        connectDatabase()
        falseData= ArrayList()

        mRefFalse.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                falseData.clear()
                for(data in snapshot.children)
                {
                    falseData.add(data.getValue(RequestFalse::class.java)!!)
                }

                val adapterFalseRequest=AdapterFalseRequest(baseContext,R.layout.view_false_request,falseData)

                grid_false_id.adapter=adapterFalseRequest


            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun connectDatabase()
    {
        val databaseCar=Firebase.database
        mRefFalse=databaseCar.getReference("False_Request")



    }
    /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_false_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_false_id.addDrawerListener(actionToggle)
        actionToggle.syncState()
        nav_side_list_id.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {

            R.id.btn_request_id->
            {
                val goToRequest= Intent(this,ActivityMain::class.java)
                startActivity(goToRequest)
                finish()
            }
            R.id.btn_add_car_id->
            {
                val goToAddCar= Intent(this,ActivityAddCar::class.java)
                startActivity(goToAddCar)
                finish()
            }
            R.id.btn_edit_car_id->
            {
                val goToEditCar= Intent(this,ActivityEditCar::class.java)
                startActivity(goToEditCar)
                finish()
            }
            R.id.btn_remove_car_id->
            {
                val goToRemoveCar= Intent(this,ActivityRemoveCar::class.java)
                startActivity(goToRemoveCar)
                finish()
            }

            R.id.btn_car_info_id->
            {
                val goToCarInfo= Intent(this,ActivityCarInfo::class.java)
                startActivity(goToCarInfo)
                finish()
            }
            R.id.btn_common_cases_id->
            {
                val goToCommonCase= Intent(this,ActivityCommonCase::class.java)
                startActivity(goToCommonCase)
                finish()
            }
            R.id.btn_accepted_request_id->
            {
                val goToAcceptedRequest= Intent(this,ActivityAcceptedRequest::class.java)
                startActivity(goToAcceptedRequest)
                finish()
            }
            R.id.btn_false_request_id->
            {
                val goToFalseRequest= Intent(this,ActivityFalseRequest::class.java)
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
        dr_false_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_false_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    /****************************************************************************************/
}