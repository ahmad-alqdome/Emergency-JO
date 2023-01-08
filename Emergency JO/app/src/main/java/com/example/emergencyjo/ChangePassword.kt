package com.example.emergencyjo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_change_password.*


class ChangePassword : BaseActivity() {

    //DataBase Reference
    private var mRefEmergencyUser: DatabaseReference? = null

    //Set position -1
    private var position: Int = -1

    //Store DataBaseEmergencyUser values in variable
    var dataBaseEmergencyUser: ArrayList<DataBaseEmergencyUser>? = null


    //To make Components in interface visible
    private fun showComponents() {

        et_password_id_changepassword.visibility = View.VISIBLE
        et_re_password_layout_changepassword.visibility = View.VISIBLE
        et_password_layout_changepassword.visibility = View.VISIBLE
        et_re_password_layout_changepassword.visibility = View.VISIBLE
        btn_change_password_changepassword_id.visibility = View.VISIBLE
        lock_icon_changepassword_id.visibility = View.INVISIBLE


    }

    //To make Components in interface invisible
    private fun hideComponents() {

        et_password_id_changepassword.visibility = View.INVISIBLE
        et_re_password_layout_changepassword.visibility = View.INVISIBLE
        et_password_layout_changepassword.visibility = View.INVISIBLE
        et_re_password_layout_changepassword.visibility = View.INVISIBLE
        btn_change_password_changepassword_id.visibility = View.INVISIBLE
        lock_icon_changepassword_id.visibility = View.INVISIBLE

    }

    /*********************************** on Create *****************************************************/
    //Main class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        //Recall Hide Components Function
        hideComponents()

        //Recall Connect Database Function
        connectDatabase()

        //Definition dataBaseEmergencyUser As ArrayList
        dataBaseEmergencyUser = ArrayList()


        //Get Status Values From EmergencyUser Database
        mRefEmergencyUser?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {
                    dataBaseEmergencyUser?.add(data.getValue(DataBaseEmergencyUser::class.java)!!)
                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })

        //Click on Check information button
        btn_check_information_changpassword_id.setOnClickListener()
        {

            //Check if PersonalID is not correct show error message
            if (!Expression.expPersonalID.matches(et_personal_id_changepasswordcheck_id.text))
                showMessageError(et_personal_id_changepasswordcheck_id, "Enter Valid Personal ID")


            //Check if Check Number is not correct show error message
            else if (!Expression.expCheck.matches(et_check_number_changepasswordcheck_id.text))
                showMessageError(et_personal_id_changepasswordcheck_id, "Enter Valid Check Number")

          //If personal id and check number is correct show the components
            else {
                for (index in 0 until dataBaseEmergencyUser!!.size) {
                    if (dataBaseEmergencyUser!![index].personalID == et_personal_id_changepasswordcheck_id.text.toString()) {
                        position = index
                        break
                    }
                }
                if (dataBaseEmergencyUser!![position].check == et_check_number_changepasswordcheck_id.text.toString()) {
                    showComponents()
                } else {

                    //Function to show alert if information is wrong
                    showAlert()
                }
            }
        }

        //Click on Change Password button
        btn_change_password_changepassword_id.setOnClickListener()
        {

            //if password did not contain like regular expression show error
            if (!Expression.expPassword.matches(et_password_id_changepassword.text.toString())) {
                showMessageError(
                    et_password_id_changepassword,
                    "Enter a Password content (8>,0-9,A-Z,a-z,character)"
                )

                //if password in two edit text not match show error
            } else if (et_password_id_changepassword.text.toString() != et_re_password_changepassword.text.toString()) {
                et_re_password_layout_changepassword.error = "Not Match"
            } else {

                //If all conditions are met Show Alert Box to Change Password
                showAlertChange()


            }
        }

    }


    //Function to connect to firebase
    private fun connectDatabase() {
        val database = Firebase.database

        //Connect to Emergency user database to Get User information
        mRefEmergencyUser = database.getReference("Emergency_user")
    }


    //Function To show error
    private fun showMessageError(item: EditText, message: String) {
        item.error = message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    //Function to show alert if information is wrong
    private fun showAlert() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage("Wrong Information")
        alertBuilder.setPositiveButton("Ok", null)
        val alert = alertBuilder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            alert.dismiss()
        }
    }


    //Function to show alert to change password
    private fun showAlertChange() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage(R.string.message_sure_change_password)
        alertBuilder.setPositiveButton(R.string.yes, null)
        alertBuilder.setNegativeButton(R.string.no, null)

        val alert = alertBuilder.create()
        alert.show()

        //if pressed on yes change password
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {

            //Get Password that user enter it and store it in password in database
            mRefEmergencyUser?.child(et_personal_id_changepasswordcheck_id.text.toString())
                ?.setValue(
                    DataBaseEmergencyUser(
                        dataBaseEmergencyUser!![position].id,
                        dataBaseEmergencyUser!![position].personalID,
                        dataBaseEmergencyUser!![position].check,
                        dataBaseEmergencyUser!![position].name,
                        dataBaseEmergencyUser!![position].mothername,
                        dataBaseEmergencyUser!![position].gender,
                        dataBaseEmergencyUser!![position].governorate,
                        dataBaseEmergencyUser!![position].birthday,

                        //Change password value in data base to password that user enter it
                        et_password_id_changepassword.text.toString(),
                        dataBaseEmergencyUser!![position].phone

                    )
                )
            hideComponents()
            alert.dismiss()
        }
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alert.dismiss()
        }


    }
}

