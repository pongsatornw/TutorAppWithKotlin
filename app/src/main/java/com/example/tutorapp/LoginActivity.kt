package com.example.tutorapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.example.tutorapp.Utility.FireStoreInitial
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        pwd_edt.transformationMethod = PasswordTransformationMethod()

        login.setOnClickListener { _ ->
            val userID = userid_edt.text.toString()
            val pwd = pwd_edt.text.toString()
            if( userID != "" && pwd != "") {
                val map = mutableMapOf<String, String>()
                map["user_id"] = userID
                map["password"] = pwd
                FireStoreInitial.initial().collection(Constants.Account.collection_name)
                        .whereEqualTo( Constants.Account.user_id, userID)
                        .whereEqualTo( Constants.Account.password,pwd)
                        .limit(1)
                        .get()
                        .addOnSuccessListener {
                            if(!it.isEmpty) {
                                userid_edt.text = null
                                pwd_edt.text = null
                                Log.d("ID", it.documents[0][Constants.User_Profile.user_id] as String)
                                Log.d("ROLE", it.documents[0]["role"] as String)
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java)
                                        .putExtra("role", it.documents[0]["role"] as String)
                                        .putExtra("user_id", it.documents[0][Constants.User_Profile.user_id] as String))
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "รหัสประจำตัวหรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show()
                                Log.d("Account", "Account not found.")
                            }
                        }
                        .addOnFailureListener {
                            it.printStackTrace()
                        }
                        .addOnCanceledListener {
                            Log.d("Account", "Canceled.")
                        }
            } else {
                Toast.makeText(this@LoginActivity, "โปรดกรอกข้อมูลรหัสประจำตัวและรหัสผ่านให้ครบถ้วน", Toast.LENGTH_SHORT).show()
            }
        }

        logout.setOnClickListener {
            finish()
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("ACTION", "ACTION DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("ACTION", "ACTION MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("ACTION", "ACTION UP")
            }
        }
        return super.dispatchTouchEvent(event)
    }

}
