package com.example.tutorapp.Service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddPeopleModel
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Year
import java.util.*


class FirestoreAddPeopleService: IntentService("FirestoreAddPeopleService") {
    //private val TAG = javaClass.simpleName

    private lateinit var dataData: MutableMap<String, Any>
    override fun setIntentRedelivery(enabled: Boolean) {
        super.setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val db = FireStoreInitial.initial()

        dataData = mutableMapOf()
        val peopleModel = intent!!.getParcelableExtra<AddPeopleModel>("people_model")
        val const = Constants.User_Profile
        dataData[const.fname] = peopleModel.fname
        dataData[const.lname] = peopleModel.lname
        dataData[const.gender] = peopleModel.gender
        dataData[const.tel_no] = peopleModel.tel_no
        dataData[const.email] = peopleModel.email
        dataData[const.role] = peopleModel.role
        val accountData = mutableMapOf<String, Any>()
        val ac_const = Constants.Account
        val count_const = Constants.Count
        accountData[ac_const.user_id] = ""
        accountData[ac_const.password] = "123456"
        accountData[ac_const.role] = peopleModel.role
        //accountData["InitialVector"] = byteArrayOf("HelloWorld".toByte())
        var year = 0
        var count = 1
        val countData = mutableMapOf<String, Any>()
        countData[count_const.st_count] =  count
        if( Build.VERSION.SDK_INT < 26) {
            year = Calendar.getInstance()[Calendar.YEAR]
        } else {
            year = Year.now(/*ZoneId.of()*/).value
        }
        if( peopleModel.role == "นักเรียน") {
            db.collection(count_const.collection_name)
                    .document(count_const.doc_name)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("isSuccessful", "True")
                            if (it.result!!.exists()) {
                                count = (it.result!![count_const.st_count] as Long).toInt() + 1
                                val newUser = year.toString().substring(1) + count.toString().padStart(4, '0')
                                accountData[ac_const.user_id] = newUser
                                countData[count_const.st_count] = count
                                Log.d("USER_ID", dataData["user_id"].toString())
                                dataData[const.user_id] = newUser
                                db.collection(count_const.collection_name)
                                        .document(count_const.doc_name)
                                        .set(countData)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                accountData[ac_const.user_id] = newUser
                                                Log.d("Info", "Update count complete")
                                                createAccount(db, accountData)
                                            } else {
                                                Log.d("Info", "Update count Fail")
                                            }
                                        }
                                        .addOnFailureListener { ex ->
                                            Log.w("Warning", ex.message)
                                        }
                            } else {
                                db.collection(count_const.collection_name)
                                        .document(count_const.doc_name)
                                        .set(countData)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val newUser = year.toString().substring(1) + (count).toString().padStart(4, '0')
                                                accountData[ac_const.user_id] = newUser
                                                count = 1
                                                dataData[const.user_id] = newUser
                                                Log.d("USER_ID", accountData["user_id"].toString())
                                                Log.d("Info", "Create count complete")
                                                createAccount(db, accountData)
                                            } else {
                                                Log.d("Info", "Create count not complete")
                                            }
                                        }
                                        .addOnFailureListener { ex ->
                                            Log.w("Warning", ex.message)
                                        }
                            }
                        } else {
                            Log.d("isSuccessful", "Fail")
                        }
                    }
                    .addOnFailureListener {
                        Log.d("Failer", "Fail")
                    }
        } else {
            dataData[const.user_id] = peopleModel.fname
            accountData[ac_const.user_id] = peopleModel.fname + ("_") + peopleModel.lname.substring(IntRange(0,1))
            accountData[ac_const.password] = peopleModel.lname
            accountData[ac_const.role] = peopleModel.role
            createAccount(db, accountData)
        }

    }

    private fun createAccount(db: FirebaseFirestore, map: MutableMap<String, Any> ){
        db.collection("Account")
                .add(map)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("Success", "Create New Account Success")
                        Toast.makeText(applicationContext, "การเพิ่มข้อมูลบุคลากรสำเร็จ", Toast.LENGTH_SHORT).show()
                        createUserProfile(db, dataData)
                    } else {
                        Log.d("Fail", "Failed")
                    }
                }.addOnFailureListener{
                    Log.w("Exception", it.message)
                }
    }

    private fun createUserProfile(db: FirebaseFirestore, map: MutableMap<String, Any> ){
        db.collection("User_Profile")
                .add(map)
                .addOnSuccessListener {
                    documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                }
                .addOnFailureListener {
                    e ->
                    Log.w("TAG", "Error adding document", e)
                }
    }
}