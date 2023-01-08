package com.example.emergencyjo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : BaseActivity() {


    private var mRefEmergencyUser:DatabaseReference?=null
    private var mRefVCivilAffairs:DatabaseReference?=null
    private var position:Int=-1

    private var dataCivilAffairs:ArrayList<DataBaseCivilAffairs>?=null

    private fun showComponents() {
        tv_name_signup_id.visibility = View.VISIBLE
        tv_birthday_signup_id.visibility = View.VISIBLE
        tv_gender_signup_id.visibility = View.VISIBLE
        tv_governorate_signup_id.visibility = View.VISIBLE
        et_password_signup_id.visibility = View.VISIBLE
        et_re_password_signup_id.visibility = View.VISIBLE
        btn_create_account_id.visibility = View.VISIBLE
        tv_mother_name_signup_id.visibility = View.VISIBLE
        lock_icon_id.visibility = View.VISIBLE
        et_phone_number_id.visibility=View.VISIBLE
        et_password_layout.visibility=View.VISIBLE
        et_phone_number_layout.visibility=View.VISIBLE
        et_re_password_layout.visibility=View.VISIBLE



    }

    private fun hideComponents() {
        tv_name_signup_id.visibility = View.INVISIBLE
        tv_birthday_signup_id.visibility = View.INVISIBLE
        tv_gender_signup_id.visibility = View.INVISIBLE
        tv_governorate_signup_id.visibility = View.INVISIBLE
        et_password_signup_id.visibility = View.INVISIBLE
        et_re_password_signup_id.visibility = View.INVISIBLE
        btn_create_account_id.visibility = View.INVISIBLE
        tv_mother_name_signup_id.visibility = View.INVISIBLE
        lock_icon_id.visibility = View.INVISIBLE
        et_phone_number_id.visibility=View.INVISIBLE
        et_password_layout.visibility=View.INVISIBLE
        et_phone_number_layout.visibility=View.INVISIBLE
        et_re_password_layout.visibility=View.INVISIBLE
    }
/*********************************** on Create *****************************************************/
    //Main class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        hideComponents()
        connectDatabase()

        dataCivilAffairs= ArrayList()


         mRefVCivilAffairs?.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(d in snapshot.children)
                {
                    val objData=d.getValue(DataBaseCivilAffairs::class.java)
                    dataCivilAffairs!!.add(objData!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {}

        })


        btn_get_data_id.setOnClickListener()
        {
            val personalId=et_personal_id_signup_id.text.toString()
            val check =et_check_number_signup_id.text.toString()

            if(!Expression.expPersonalID.matches(personalId)||personalId.isEmpty()) {

                showMessageError(et_personal_id_signup_id,"Please Enter Personal ID Correct")

            } else  if(!Expression.expCheck.matches(check)||check.isEmpty()) {
                showMessageError(et_check_number_signup_id,"Enter Check Number Correct")


            } else
            {
                for(i in 0 until dataCivilAffairs!!.size)
                {
                    if(dataCivilAffairs!![i].personalID==personalId)
                    {
                        position=i
                        break
                    }

                }
                if(position==-1)
                {
                    Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show()
                }



                else if(dataCivilAffairs!![position].check==check)
                {


                    mRefEmergencyUser?.child(dataCivilAffairs!![position].id!!)?.addListenerForSingleValueEvent(object :ValueEventListener
                    {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists())
                                showAlertFoundAccount()

                        else
                        {
                            showComponents()
                            tv_name_signup_id.text=dataCivilAffairs!![position].name
                            tv_birthday_signup_id.text=dataCivilAffairs!![position].birthday
                            tv_gender_signup_id.text=dataCivilAffairs!![position].gender
                            tv_governorate_signup_id.text=dataCivilAffairs!![position].governorate
                            tv_mother_name_signup_id.text=dataCivilAffairs!![position].mothername
                        }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })


                }
            }
        }
        btn_create_account_id.setOnClickListener()
        {
            val password=et_password_signup_id.text.toString()
            val rePassword=et_re_password_signup_id.text.toString()
            val phone=et_phone_number_id.text.toString()
            if(!Expression.expPhone.matches(phone))
            {
                showMessageError(et_phone_number_id,"Enter a Phone like (078/7/9/xxxxxxx)")
            }
            else if(!Expression.expPassword.matches(password))
            {
                showMessageError(et_password_signup_id,"Enter a Password content (8>,0-9,A-Z,a-z,character)")
            }
            else if (password != rePassword)
                et_password_layout.error="Password Not Match"
            else
            {
                val obj=DataBaseEmergencyUser(dataCivilAffairs!![position].id,dataCivilAffairs!![position].personalID ,
                    dataCivilAffairs!![position].check,dataCivilAffairs!![position].name,dataCivilAffairs!![position].mothername,
                    dataCivilAffairs!![position].gender,dataCivilAffairs!![position].governorate,dataCivilAffairs!![position].birthday,
                   password,phone)
                mRefEmergencyUser?.child(dataCivilAffairs!![position].id!!)?.setValue(obj)

                savedIdToSharedPreferences(obj) // save id in file shared preferences

                val goToMain=Intent(this,Main::class.java)
                startActivity(goToMain)                             // got main activity

                finish() // end activity when store data in database and shared preferences
            }
        }

    }

    private fun showAlertFoundAccount() {
        val alertBuilder=AlertDialog.Builder(this)
        alertBuilder.setMessage("The account is registered , Go to login")
        alertBuilder.setPositiveButton(R.string.btn_login,null)
        alertBuilder.setNeutralButton(R.string.btn_cancel_alertdialog,null)
        val alertDialog=alertBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            finish()
        }
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener()
        {
            alertDialog.dismiss()
        }
    }

    private fun savedIdToSharedPreferences(data:DataBaseEmergencyUser) {

        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()


        editor.putString(UserProperties.USER_ID,data.id)
        editor.putString(UserProperties.USER_PERSONAL_ID,data.personalID)
        editor.putString(UserProperties.USER_CHECK,data.check)
        editor.putString(UserProperties.USER_NAME,data.name)
        editor.putString(UserProperties.USER_MOTHERNAME,data.mothername)
        editor.putString(UserProperties.USER_BIRTHDAY,data.birthday)
        editor.putString(UserProperties.USER_GOVERNORATE,data.governorate)
        editor.putString(UserProperties.USER_GENDER,data.gender)
        editor.putString(UserProperties.USER_PASSWORD,data.password)
        editor.putString(UserProperties.USER_PHONE,data.phone)


        editor.apply()
    }


    private fun connectDatabase()
    {
        val database= Firebase.database
        mRefVCivilAffairs=database.getReference("Civil Affairs")
        mRefEmergencyUser=database.getReference("Emergency_user")

    }
    private fun showMessageError(item: EditText, message: String) {
        item.error = message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

}



