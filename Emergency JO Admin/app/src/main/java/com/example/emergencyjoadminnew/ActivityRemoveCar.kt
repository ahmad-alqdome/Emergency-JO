package com.example.emergencyjoadminnew

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.activity_remove_car.*
import kotlinx.android.synthetic.main.activity_remove_car.nav_side_list_id

class ActivityRemoveCar : BaseActivity() ,TextWatcher,NavigationView.OnNavigationItemSelectedListener {

    // global variables
    private lateinit var myCar:DatabaseCar
    private lateinit var mRefCar: DatabaseReference
    lateinit var dataCar:ArrayList<DatabaseCar>
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_car)

        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        connectDatabase()
        dataCar = ArrayList()
        btn_remove_car_remove_id.visibility= View.INVISIBLE

        mRefCar.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataCar.clear()
                for (d in snapshot.children) {
                    dataCar.add(d.getValue(DatabaseCar::class.java)!!)
                }
                val adapterRemove = AdapterRemove(baseContext, R.layout.view_car, dataCar)
                adapterRemove.notifyDataSetChanged()
                grid_search?.adapter = adapterRemove

            }
            override fun onCancelled(error: DatabaseError) {}
        })

        et_car_number_remove_id.addTextChangedListener(this)
        registerForContextMenu(grid_search)

        btn_remove_car_remove_id.setOnClickListener()
        {
            showAlertSure(et_car_number_remove_id.text.toString())
        }


    }

    override fun onStart() {
        super.onStart()

        grid_search.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->

                myCar = dataCar[position]
                false
            }
    }

    private fun connectDatabase()
    {
        val database= Firebase.database
        mRefCar=database.getReference("Car")

    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    )
    {

        val menuInflater= MenuInflater(this)
        menuInflater.inflate(R.menu.list,menu)
    }

    // on select delete in context menu will remove data in fire base
    override fun onContextItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.delete_item_id) {
            showAlertSure(myCar.carNumber)
        }
        return true
    }


    private fun showAlertSure(item:String)
    {
        val alertBuilder= AlertDialog.Builder(this)
        alertBuilder.setMessage("You Are Sure To Remove")
        alertBuilder.setPositiveButton("Yes", null)
        alertBuilder.setNegativeButton("No",null)
        val alertDialog=alertBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            mRefCar.child(item).removeValue()
            alertDialog.dismiss()
//            val f:FragmentManager=requireFragmentManager()
//            transaction=f.beginTransaction()
//            transaction.replace(R.id.content_id,FragmentRemoveCar())
//            transaction.commit()

        }
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alertDialog.dismiss()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {


        mRefCar.child(et_car_number_remove_id.text.toString()).addListenerForSingleValueEvent(object:ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()&&Exp.expCarNumber.matches(et_car_number_remove_id.text.toString()))
                {
                    btn_remove_car_remove_id.visibility=View.VISIBLE
                }
                else
                {
                    btn_remove_car_remove_id.visibility=View.INVISIBLE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_remove_car_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_remove_car_id.addDrawerListener(actionToggle)
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
        dr_remove_car_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_remove_car_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    /****************************************************************************************/
}