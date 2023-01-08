package com.example.emergencyjo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_choose_safety.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.drawer_main_id
import kotlinx.android.synthetic.main.activity_main.nav_side_list_id
import kotlinx.android.synthetic.main.activity_medical_saftey.view.*
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.header_side_list.view.*

class ChooseSafety : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    //Definition  userName as String
    private lateinit var userName:String

    //Definition  toolbar as Toolbar
    private lateinit var toolbar: Toolbar

    //Reference Database
    private  lateinit var mRefData: DatabaseReference

    //Definition  headerView as View
    private lateinit var headerView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_safety)
        toolbar = findViewById(R.id.header_id)
       setSupportActionBar(toolbar)
       supportActionBar?.title = ""

        //Connect Bar Function
        connectActionbar()

        //Connect DataBase Function
        connectDataBase()

        //Store User Name in GetName (Using Get Name Function)
        userName=getName()


        headerActionBar()





    }

    override fun onStart() {
        super.onStart()


        //click on button to got to FireSafety activity
    btn_dealing_with_fires_id.setOnClickListener()
    {
        val goToFireSafety=Intent(this,FireSafety::class.java)
        startActivity(goToFireSafety)
    }

        //click on button to got to MedicalSafety activity
        btn_medical_saftey_id.setOnClickListener()
        {
            val goToMedicalSafety=Intent(this,MedicalSaftey::class.java)
            startActivity(goToMedicalSafety)
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
        val actionToggle= ActionBarDrawerToggle(this,drawer_safety_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        drawer_safety_id.addDrawerListener(actionToggle)
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
                finish()
            }


            //Side Bar to go to Setting
            R.id.setting_side_list_id -> {
                val goToSetting = Intent(this, UserSetting::class.java)
                startActivity(goToSetting)
                 finish()
            }

            //Side Bar to go to Home
            R.id.home_side_list_id -> {
                val goToMain = Intent(this, Main::class.java)
                startActivity(goToMain)
                finish()
            }
        }

        //Toast.makeText(this, "$item", Toast.LENGTH_SHORT).show()
        closeDrawer()
        return true
    }

    //Fun to close drawer when press back
    override fun onBackPressed() {
        if(drawer_safety_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    //Fun to close drawer
    private fun closeDrawer()
    {
        drawer_safety_id.closeDrawer(GravityCompat.START)

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
        val database= Firebase.database
        //Connect to Emergency user database to get name
        mRefData=database.getReference("Emergency_user")

    }
}