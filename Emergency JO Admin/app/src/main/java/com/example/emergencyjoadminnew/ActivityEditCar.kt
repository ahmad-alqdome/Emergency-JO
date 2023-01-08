package com.example.emergencyjoadminnew

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.activity_edit_car.*

class ActivityEditCar : BaseActivity(),TextWatcher,NavigationView.OnNavigationItemSelectedListener{

    //global variables

    private lateinit var mRefCar: DatabaseReference
    private var position: Int = -1
    private lateinit var carList: ArrayList<DatabaseCar>
    var oldUser:String?=null
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_car)

        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        connectDatabase()

        hideComponents()
        carList = ArrayList()
        et_car_number_edit_id.addTextChangedListener(this)


        mRefCar.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {
                    carList.add(data.getValue(DatabaseCar::class.java)!!)
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    override fun onStart() {
        super.onStart()

        btn_get_data_edit_id.setOnClickListener()
        {

            for (i in 0 until carList.size) {
                if (carList[i].carNumber==et_car_number_edit_id.text.toString()) {
                    position = i
                    break
                }
            }

            if (position == -1) {
                et_car_number_edit_id.error = "The Car Not Found"
            } else {
                showComponents()
                oldUser=carList[position].carNumber
                et_car_name_edit_id.setText(carList[position].carName)
                et_car_model_edit_id.setText(carList[position].carModel)
                et_military_name_edit_id.setText(carList[position].militaryName)
                et_military_number_edit_id.setText(carList[position].militaryNumber)
                et_password_edit_id.setText(carList[position].password)
                et_re_password_edit_id.setText(carList[position].password)
                if(carList[position].typeCar=="FireFighter")
                {
                    sp_type_car_edit_id.setSelection(1)
                }
                else
                {
                    sp_type_car_edit_id.setSelection(0)
                }
            }

        }//end get button


        btn_edit_information_id.setOnClickListener()
        {
            if(!Exp.expCarNumber.matches(et_car_number_edit_id.text.toString()))
            {
                showMessageError(et_car_number_edit_id,"Enter a car number valid")
            }
            else if(!Exp.expCarName.matches(et_car_name_edit_id.text.toString()))
            {
                showMessageError(et_car_name_edit_id,"Enter a car name valid")
            }
            else if(!Exp.expCarModel.matches(et_car_model_edit_id.text.toString()))
            {
                showMessageError(et_car_model_edit_id,"Enter a car model valid")
            }
            else if(!Exp.expMilitaryName.matches(et_military_name_edit_id.text.toString()))
            {
                showMessageError(et_military_name_edit_id,"Enter a name valid")
            }
            else  if(!Exp.expMilitaryNumber.matches(et_military_number_edit_id.text.toString()))
            {
                showMessageError(et_military_number_edit_id,"Enter a Military number valid")
            }

            else if(!Exp.expPassword.matches(et_password_edit_id.text.toString()))
            {
                showMessageError(et_password_edit_id,"Enter a Password ( 8> , A , a , $)")
            }

            else if (et_password_edit_id.text.toString() != et_re_password_edit_id.text.toString()) {
                et_re_password_layout_form_edit_id.error = "Password Not Match"
            }
            else {

                mRefCar.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (!(snapshot.child(et_car_number_edit_id.text.toString()).exists())
                            ||et_car_number_edit_id.text.toString()==oldUser) {


                            alertEdit()

                        }//end if

                        else
                        {
                            et_car_number_edit_id.error="number car is exist"
                        }//end else

                    }//end data change


                    override fun onCancelled(error: DatabaseError) {}

                }) // end add value listener
            }//end else
        }// end button
    }

    private fun alertEdit() {
         val alertBuilder= AlertDialog.Builder(this)
        alertBuilder.setMessage("Are You Sure To Edit Information")
        alertBuilder.setPositiveButton("Yes",null)
        alertBuilder.setNegativeButton("No",null)
        val alert=alertBuilder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
        removeValue()
        addValue()
        emptyFields()
        hideComponents()
        alert.dismiss()
        }//end positive

    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
    {
        alert.dismiss()
    }

    }

    private fun connectDatabase() {
        val database = Firebase.database
        mRefCar = database.getReference("Car")
    }


    private fun removeValue()
    {
        mRefCar.child(oldUser.toString()).removeValue()
    }

    private fun showMessageError(item: EditText, message:String)
    {
        item.error=message
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()

    }


    private fun emptyFields() {
        et_car_number_edit_id.setText("")
        et_car_name_edit_id.setText("")
        et_car_model_edit_id.setText("")
        et_military_name_edit_id.setText("")
        et_military_number_edit_id.setText("")
        tv_show_username_edit_id.text=""
        et_password_edit_id.setText("")
        et_re_password_edit_id.setText("")
    }

    private fun addValue() {
        mRefCar.child(et_car_number_edit_id.text.toString()).setValue(
            DatabaseCar(
                et_car_number_edit_id.text.toString(),
                et_car_name_edit_id.text.toString(),
                et_car_model_edit_id.text.toString(),
                et_military_name_edit_id.text.toString(),
                et_military_number_edit_id.text.toString(),
                tv_show_username_edit_id.text.toString(),
                et_password_edit_id.text.toString(),
                sp_type_car_edit_id.selectedItem.toString(),
                "Available"
            )
        )
    }
    /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_edit_car_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_edit_car_id.addDrawerListener(actionToggle)
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
        dr_edit_car_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_edit_car_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    /****************************************************************************************/
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        tv_show_username_edit_id.text = et_car_number_edit_id.text.toString()
    }

    override fun afterTextChanged(s: Editable?) {
        if(et_car_number_edit_id.text.length==2)
            et_car_number_edit_id.append("-")
    }
    private fun showComponents() {
        tv_car_name_edit_id.visibility = View.VISIBLE
        et_car_name_edit_id.visibility = View.VISIBLE
        tv_car_model_edit_id.visibility = View.VISIBLE
        et_car_model_edit_id.visibility = View.VISIBLE
        tv_military_name_edit_id.visibility = View.VISIBLE
        et_military_name_edit_id.visibility = View.VISIBLE
        tv_military_number_edit_id.visibility = View.VISIBLE
        et_military_number_edit_id.visibility = View.VISIBLE
        tv_username_edit_id.visibility = View.VISIBLE
        tv_show_username_edit_id.visibility = View.VISIBLE
        et_password_edit_id.visibility = View.VISIBLE
        tv_password_edit_id.visibility = View.VISIBLE
        tv_re_password_edit_id.visibility = View.VISIBLE
        et_re_password_edit_id.visibility = View.VISIBLE
        et_password_layout_form_edit_id.visibility = View.VISIBLE
        tv_car_name_edit_id.visibility = View.VISIBLE
        et_re_password_layout_form_edit_id.visibility = View.VISIBLE
        btn_edit_information_id.visibility = View.VISIBLE
        sp_type_car_edit_id.visibility= View.VISIBLE
        tv_car_type_edit_id.visibility= View.VISIBLE

    }
    private fun hideComponents() {
        tv_car_name_edit_id.visibility = View.GONE
        et_car_name_edit_id.visibility = View.GONE
        tv_car_model_edit_id.visibility = View.GONE
        et_car_model_edit_id.visibility = View.GONE
        tv_military_name_edit_id.visibility = View.GONE
        et_military_name_edit_id.visibility = View.GONE
        tv_military_number_edit_id.visibility = View.GONE
        et_military_number_edit_id.visibility = View.GONE
        tv_username_edit_id.visibility = View.GONE
        tv_show_username_edit_id.visibility = View.GONE
        et_password_edit_id.visibility = View.GONE
        tv_password_edit_id.visibility = View.GONE
        tv_re_password_edit_id.visibility = View.GONE
        et_re_password_edit_id.visibility = View.GONE
        et_password_layout_form_edit_id.visibility = View.GONE
        tv_car_name_edit_id.visibility = View.GONE
        et_re_password_layout_form_edit_id.visibility = View.GONE
        btn_edit_information_id.visibility = View.GONE
        sp_type_car_edit_id.visibility= View.GONE
        tv_car_type_edit_id.visibility= View.GONE

    }
}