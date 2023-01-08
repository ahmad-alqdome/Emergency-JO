package com.example.emergencyjoadminnew

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import kotlinx.android.synthetic.main.activity_common_case.*
import kotlinx.android.synthetic.main.activity_common_case.nav_side_list_id

class ActivityCommonCase : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    //global variables
    private lateinit var commonCases: CommonCases
    private lateinit var databaseCommon: DatabaseReference
    lateinit var dataCases:ArrayList<CommonCases>
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_case)



        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        connectDatabase()

        dataCases = ArrayList()

        registerForContextMenu(grid_common_cases_id)

        databaseCommon.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataCases.clear()
                for (d in snapshot.children) {
                    dataCases.add(d.getValue(CommonCases::class.java)!!)
                }
                val adapterRemoveCommon = AdapterRemoveCasess(baseContext, R.layout.view_cases, dataCases)
                adapterRemoveCommon.notifyDataSetChanged()
                grid_common_cases_id.adapter = adapterRemoveCommon


            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        grid_common_cases_id.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                commonCases = dataCases[position]
                false
            }

        btn_case_add.setOnClickListener{

            if(et_case_name.text.isEmpty())
                et_case_name.error="Can't Empty"
            else
            {
                databaseCommon.child(et_case_name.text.toString()).setValue(CommonCases(
                    et_case_name.text.toString(),
                    et_option_1.text.toString(),
                    et_option_2.text.toString(),
                    et_option_3.text.toString(),
                ))
                et_case_name.setText("")
                et_option_1.setText("")
                et_option_2.setText("")
                et_option_3.setText("")

            }
        }

    } // end create

    private fun connectDatabase() {
        val database= Firebase.database
        databaseCommon=database.getReference("Common_Accident")
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        val menuInflater= MenuInflater(this)
        menuInflater.inflate(R.menu.list,menu)
    }
    // on select delete in context menu will remove data in fire base
    override fun onContextItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.delete_item_id) {
            showAlertSure(commonCases.accident_name)
        }
        return true
    }

    private fun showAlertSure(item: String) {
        val alertBuilder= android.app.AlertDialog.Builder(this)
        alertBuilder.setMessage("You Are Sure To Remove")
        alertBuilder.setPositiveButton("Yes", null)
        alertBuilder.setNegativeButton("No",null)
        val alertDialog=alertBuilder.create()
        alertDialog.show()
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            databaseCommon.child(item).removeValue()
            alertDialog.dismiss()

        }
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alertDialog.dismiss()
        }
    }
    /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_common_cases_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_common_cases_id.addDrawerListener(actionToggle)
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
        dr_common_cases_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_common_cases_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    /****************************************************************************************/



}