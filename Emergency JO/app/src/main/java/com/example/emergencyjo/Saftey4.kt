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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.drawer_main_id
import kotlinx.android.synthetic.main.activity_main.nav_side_list_id
import kotlinx.android.synthetic.main.activity_saftey4.*
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.header_side_list.view.*

class Saftey4 : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var userName:String
    private lateinit var toolbar: Toolbar
    private  lateinit var mRefData: DatabaseReference
    private lateinit var headerView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saftey4)
        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        connectDataBase()
        userName=getName()
        headerActionBar()



    }

    private fun  headerActionBar()
    {
        headerView =nav_side_list_id.getHeaderView(0)
        headerView.name_user_side_list_id.text=userName

    }
    @JvmName("getName1")
    private fun getName(): String
    {
        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        return sharedPreferences.getString(UserProperties.USER_NAME,"").toString()
    }
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,drawer_safety4_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        drawer_safety4_id.addDrawerListener(actionToggle)
        actionToggle.syncState()
        nav_side_list_id.setNavigationItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {


            R.id.logout_side_list_id -> {
                editIdToSharedPreferences()
                val goToLogin = Intent(this, Login::class.java)
                startActivity(goToLogin)
                finish()
            }


            R.id.safety_Instructions_side_list_id -> {
                val goToSafety = Intent(this, ChooseSafety::class.java)
                startActivity(goToSafety)
                finish()
            }

            R.id.setting_side_list_id -> {
                val goToSetting = Intent(this, UserSetting::class.java)
                startActivity(goToSetting)
                finish()
            }

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

    override fun onBackPressed() {
        if(drawer_safety4_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    private fun closeDrawer()
    {
        drawer_safety4_id.closeDrawer(GravityCompat.START)

    }
    private fun editIdToSharedPreferences() {

        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putString(UserProperties.USER_ID,"0")
        editor.apply()
    }
    private fun connectDataBase()
    {
        val database= Firebase.database
        mRefData=database.getReference("Emergency_user")

    }
}