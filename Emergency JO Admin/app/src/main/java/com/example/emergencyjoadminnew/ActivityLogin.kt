package com.example.emergencyjoadminnew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : BaseActivity() {

    // global variables
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        checkAccount()


        btn_login_id.setOnClickListener()
        {

            val email = et_email_login_id.text.toString()
            val pass = et_password_login_id.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, ActivityMain::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Please Check Email and Password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        } // end button


    }


    private fun checkAccount(){
    if (firebaseAuth.currentUser != null) {
        val intent = Intent(this, ActivityMain::class.java)
        startActivity(intent)
        finish()
    } // end checkAccount
    }
}