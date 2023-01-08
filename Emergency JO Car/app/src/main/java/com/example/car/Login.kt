package com.example.car

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login : BaseActivity(),TextWatcher {

    private lateinit var mRefCar:DatabaseReference
    private lateinit var carData: ArrayList<DatabaseCar>
    private var position:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username=getUsername()
        if(username!="")
        {
            val goToMainActivity=Intent(this,MainActivity::class.java)
            startActivity(goToMainActivity)
            finish()
        }

        carData= ArrayList()
        connectDatabase()
        btn_login_id.isEnabled=false
        et_username_login_id.addTextChangedListener(this)
        et_password_login_id.addTextChangedListener(this)




        mRefCar.addValueEventListener(object:ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children)
                {
                    carData.add(data.getValue(DatabaseCar::class.java)!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }

    override fun onStart() {
        super.onStart()

        btn_login_id.setOnClickListener()
        {
            if(!Exp.expCarNumber.matches(et_username_login_id.text))
                et_username_login_id.error="Enter Correct Username"
            else
            {
                for(index in 0 until carData.size)
                {
                    if(carData[index].username==et_username_login_id.text.toString())
                    {
                        position=index
                        break
                    }
                }

                if(position==-1)
                {
                    Toast.makeText(baseContext, "The user name not correct", Toast.LENGTH_SHORT).show()
                }
                else if(carData[position].password!=et_password_login_id.text.toString())
                {
                    Toast.makeText(baseContext,"The Username or Password is Not Correct ",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    savedIdToSharedPreferences()
                    mRefCar.child(getUsername()).child("status").setValue("Available")
                    val goToMainActivity=Intent(this,MainActivity::class.java)
                    startActivity(goToMainActivity)
                }
            }
        }
    }

    private fun getUsername():String
    {

            val sharedPreferences=getSharedPreferences(CarProperties.FILE_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(CarProperties.USERNAME,"").toString()

    }
    private fun savedIdToSharedPreferences()
    {

        val sharedPreferences: SharedPreferences =getSharedPreferences(CarProperties.FILE_NAME, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()


        editor.putString(CarProperties.CAR_NUMBER,carData[position].carNumber)
        editor.putString(CarProperties.CAR_NAME,carData[position].carName)
        editor.putString(CarProperties.CAR_MODEL,carData[position].carModel)
        editor.putString(CarProperties.CAR_TYPE,carData[position].typeCar)
        editor.putString(CarProperties.MILITARY_NAME,carData[position].militaryName)
        editor.putString(CarProperties.MILITARY_NUMBER,carData[position].militaryNumber)
        editor.putString(CarProperties.USERNAME,carData[position].username)
        editor.putString(CarProperties.PASSWORD,carData[position].password)


        editor.apply()
    }

    private fun connectDatabase()
    {
        val database= Firebase.database
        mRefCar=database.getReference("Car")

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
    {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
    {
    }

    override fun afterTextChanged(s: Editable?)
    {
                if(et_username_login_id.text.length==2)
                {
                    et_username_login_id.text.append("-")
                }
        if(et_username_login_id.text.isNotEmpty()&&et_password_login_id.text.isNotEmpty())
            btn_login_id.isEnabled=true

    }
}