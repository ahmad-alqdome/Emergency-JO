package com.example.emergencyjo


import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_forgetpassword.*


class forgetpassword : BaseActivity() {


    private var mRefEmergencyUser: DatabaseReference? = null
    private var position: Int = -1
    var dataBaseEmergencyUser: ArrayList<DataBaseEmergencyUser>? = null


    private fun showComponents() {

        et_password_id_forgetpassword.visibility = View.VISIBLE
        et_re_password_layout_forgetpassword.visibility = View.VISIBLE
        et_password_layout_forgetpassword.visibility = View.VISIBLE
        et_re_password_layout_forgetpassword.visibility = View.VISIBLE
        btn_change_password_forgetpassword_id.visibility = View.VISIBLE
        lock_icon_forgetpassword_id.visibility = View.INVISIBLE


    }

    private fun hideComponents() {

        et_password_id_forgetpassword.visibility = View.INVISIBLE
        et_re_password_layout_forgetpassword.visibility = View.INVISIBLE
        et_password_layout_forgetpassword.visibility = View.INVISIBLE
        et_re_password_layout_forgetpassword.visibility = View.INVISIBLE
        btn_change_password_forgetpassword_id.visibility = View.INVISIBLE
        lock_icon_forgetpassword_id.visibility = View.INVISIBLE

    }

    /*********************************** on Create *****************************************************/
    //Main class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpassword)
        hideComponents()
        connectDatabase()

        dataBaseEmergencyUser = ArrayList()

        mRefEmergencyUser?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {
                    dataBaseEmergencyUser?.add(data.getValue(DataBaseEmergencyUser::class.java)!!)
                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })

        btn_check_information_forgetpassword_id.setOnClickListener()
        {
            if (!Expression.expPersonalID.matches(et_personal_id_forgetpassword_id.text))
                showMessageError(et_personal_id_forgetpassword_id, "Enter Valid Personal ID")
            else if (!Expression.expCheck.matches(et_check_number_forgetpassword_id.text))
                showMessageError(et_personal_id_forgetpassword_id, "Enter Valid Check Number")
            else {
                for (index in 0 until dataBaseEmergencyUser!!.size) {
                    if (dataBaseEmergencyUser!![index].personalID == et_personal_id_forgetpassword_id.text.toString()) {
                        position = index
                        break
                    }
                }
                if (dataBaseEmergencyUser!![position].check == et_check_number_forgetpassword_id.text.toString()) {
                    showComponents()
                } else {
                    showAlert()
                }
            }
        }
        btn_change_password_forgetpassword_id.setOnClickListener()
        {
            if (!Expression.expPassword.matches(et_password_id_forgetpassword.text.toString())) {
                showMessageError(
                    et_password_id_forgetpassword,
                    "Enter a Password content (8>,1,A,a,$)"
                )
            } else if (et_password_id_forgetpassword.text.toString() != et_re_password_forgetpassword.text.toString()) {
                et_re_password_layout_forgetpassword.error = "Not Match"
            } else {
                showAlertChange()
            }

        }

    }

    private fun connectDatabase() {
        val database = Firebase.database
        mRefEmergencyUser = database.getReference("Emergency_user")
    }

    private fun showMessageError(item: EditText, message: String) {
        item.error = message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    private fun showAlert() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage(R.string.message_wrong_information)
        alertBuilder.setPositiveButton(R.string.ok, null)
        val alert = alertBuilder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            alert.dismiss()
        }
    }

    private fun showAlertChange() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage(getString(R.string.message_sure_change_password))
        alertBuilder.setPositiveButton(R.string.yes, null)
        alertBuilder.setNegativeButton(R.string.no, null)

        val alert = alertBuilder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            mRefEmergencyUser?.child(et_personal_id_forgetpassword_id.text.toString())
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
                        et_password_id_forgetpassword.text.toString(),
                        dataBaseEmergencyUser!![position].phone

                    )
                )
            val intent = Intent(this , Login::class.java)
            startActivity(intent)
            hideComponents()
            alert.dismiss()
        }
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alert.dismiss()
        }


    }
}

